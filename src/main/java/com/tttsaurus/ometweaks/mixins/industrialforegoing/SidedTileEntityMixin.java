package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.CustomElectricMachine;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.IMachineWithCapacitor;
import crazypants.enderio.api.capacitor.CapabilityCapacitorData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SidedTileEntity.class)
public class SidedTileEntityMixin
{
    @WrapMethod(method = "isPaused", remap = false)
    public boolean isPaused(Operation<Boolean> original)
    {
        if (!IndustrialForegoingModule.ENABLE_IF_EIO_CAPACITOR_COMPAT) return original.call();

        SidedTileEntity this0 = (SidedTileEntity)(Object)this;

        if (this0 instanceof CustomElectricMachine electricMachine)
        {
            IMachineWithCapacitor machine = (IMachineWithCapacitor)electricMachine;
            ItemStackHandler handler = machine.getCapacitorStackHandler();

            if (handler == null) return true;

            ItemStack itemStack = handler.getStackInSlot(0);

            if (itemStack.isEmpty()) return true;
            if (!itemStack.hasCapability(CapabilityCapacitorData.getCapNN(), null)) return true;

            return original.call();
        }
        else
            return original.call();
    }
}
