package com.tttsaurus.ometweaks.mixins.scp;

import alexiy.secure.contain.protect.SCPItem;
import alexiy.secure.contain.protect.Utils;
import alexiy.secure.contain.protect.capability.Capabilities;
import alexiy.secure.contain.protect.capability.bloodstone.IBloodstoneCapability;
import alexiy.secure.contain.protect.capability.cowbell.ICowbellCapability;
import alexiy.secure.contain.protect.capability.killedentities.IKilledEntitiesCapability;
import alexiy.secure.contain.protect.capability.lostitems.ILostItemsCapability;
import alexiy.secure.contain.protect.capability.shadowinfestation.IShadowInfestationCapability;
import alexiy.secure.contain.protect.capability.sleepdeprivation.ISleepDeprivationCapability;
import alexiy.secure.contain.protect.capability.zombievirus.Infection;
import alexiy.secure.contain.protect.events.CommonEvents;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.scp.SCPModule;
import com.tttsaurus.ometweaks.integration.scp.capability.SCPNegativeCapabilities;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(CommonEvents.class)
public class CommonEventsMixin
{
    @Unique
    private static final Map<UUID, SCPNegativeCapabilities> OME_Tweaks$negativeCapabilityCache = new HashMap<>();

    /**
     * @author tttsaurus
     * @reason To disable capabilities by config, and this method is modified from alexiy.secure.contain.protect.events.CommonEvents.onPlayerTick
     */
    @WrapMethod(method = "onPlayerTick", remap = false)
    @SubscribeEvent
    private static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent, Operation<Void> original)
    {
        if (playerTickEvent.player.world.isRemote) return;
        if (!(playerTickEvent.player instanceof EntityPlayerMP player)) return;

        SCPNegativeCapabilities capabilities = OME_Tweaks$negativeCapabilityCache.get(player.getUniqueID());
        if (capabilities == null)
        {
            capabilities = new SCPNegativeCapabilities();
            capabilities.sleepDeprivation = (ISleepDeprivationCapability)player.getCapability(Capabilities.SlEEP_DEPRIVATION, (EnumFacing)null);
            capabilities.shadowInfestation = (IShadowInfestationCapability)player.getCapability(Capabilities.SHADOW_INFESTATION_CAPABILITY, (EnumFacing)null);
            capabilities.bloodstone = (IBloodstoneCapability)player.getCapability(Capabilities.BLOODSTONE_CAPABILITY, (EnumFacing)null);
            capabilities.infection = (Infection)player.getCapability(Capabilities.INFECTIONABLE, (EnumFacing)null);
            capabilities.killedEntities = (IKilledEntitiesCapability)player.getCapability(Capabilities.KILLED_ENTITIES_CAPABILITY, (EnumFacing)null);
            capabilities.cowbell = (ICowbellCapability)player.getCapability(Capabilities.COWBELL_CAPABILITY, (EnumFacing)null);
            capabilities.lostItems = (ILostItemsCapability)player.getCapability(Capabilities.LOST_ITEMS_CAPABILITY, (EnumFacing)null);
            OME_Tweaks$negativeCapabilityCache.put(player.getUniqueID(), capabilities);
        }

        if (SCPModule.DISABLE_SCP_SLEEP_DEPRIVATION_CAP)
            capabilities.sleepDeprivation.cure(player);

        if (SCPModule.DISABLE_SCP_SHADOW_INFESTATION_CAP)
        {
            capabilities.shadowInfestation.setInfestationStage(0);
            capabilities.shadowInfestation.setInfestationTime(-1);
        }

        if (SCPModule.DISABLE_SCP_BLOODSTONE_CAP)
            capabilities.bloodstone.resetBloodstoneState();

        if (SCPModule.DISABLE_SCP_INFECTION_CAP)
        {
            capabilities.infection.setActive(false);
            capabilities.infection.setDuration(0);
        }

        if (SCPModule.DISABLE_SCP_KILLED_ENTITIES_CAP)
            capabilities.killedEntities.getStoredEntities().clear();

        if (SCPModule.DISABLE_SCP_COWBELL_CAP)
            capabilities.cowbell.setHasRungCowbell(false);

        if (SCPModule.DISABLE_SCP_LOST_ITEMS_CAP)
            capabilities.lostItems.getLostItems().clear();

        if (!SCPModule.DISABLE_SCP_SLEEP_DEPRIVATION_CAP)
            capabilities.sleepDeprivation.applySleepDeprivation(player);
        if (!SCPModule.DISABLE_SCP_BLOODSTONE_CAP)
            if (Utils.isPlayerInSurvivalMode(player))
                capabilities.bloodstone.handleBloodstoneEffects(playerTickEvent.player);

        ItemStack heldItem;
        SCPItem scpItem;
        if (player.getActiveHand() != null)
        {
            heldItem = player.getHeldItem(player.getActiveHand());
            if (heldItem.getItem() instanceof SCPItem)
            {
                scpItem = (SCPItem)heldItem.getItem();
                if (scpItem.isRechargable() && scpItem.shouldItemRecharge(heldItem))
                    SCPItem.dechargeItem(heldItem, player);
            }
        }

        if (player.getHeldItemOffhand() != null)
        {
            heldItem = player.getHeldItem(EnumHand.OFF_HAND);
            if (heldItem.getItem() instanceof SCPItem)
            {
                scpItem = (SCPItem)heldItem.getItem();
                if (scpItem.isRechargable() && scpItem.shouldItemRecharge(heldItem))
                    SCPItem.dechargeItem(heldItem, player);
            }
        }
    }
}
