package com.tttsaurus.ometweaks.integration.industrialforegoing;

import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class InfinityDrillBlacklist
{
    @SubscribeEvent
    public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event)
    {
        EntityPlayer player = event.getHarvester();
        if (player == null) return;
        World world = player.world;
        if (world.isRemote) return;

        ResourceLocation rl = player.getHeldItemMainhand().getItem().getRegistryName();
        if (rl == null) return;
        if (!rl.toString().equals("industrialforegoing:infinity_drill")) return;

        IBlockState blockState = event.getState();
        Block block = blockState.getBlock();

        ItemStack itemStack = new ItemStack(block, 1, block.getMetaFromState(blockState));
        for (ItemStack blacklistItemStack : OMEConfig.IF_INFINITY_DRILL_BLACKLIST)
            if (itemStack.isItemEqual(blacklistItemStack))
            {
                event.getDrops().clear();
                return;
            }
    }
}
