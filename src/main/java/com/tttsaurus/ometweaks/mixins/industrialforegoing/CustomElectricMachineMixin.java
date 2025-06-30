package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.CustomElectricMachine;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.CapacitorInventoryInjector;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.IMachineWithCapacitor;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CustomElectricMachine.class)
public class CustomElectricMachineMixin implements IMachineWithCapacitor
{
    @Unique
    private ItemStackHandler OME_Tweaks$capacitorStackHandler;

    @Override
    public ItemStackHandler getCapacitorStackHandler()
    {
        return OME_Tweaks$capacitorStackHandler;
    }

    @WrapMethod(method = "initializeInventories", remap = false)
    public void extraInventory(Operation<Void> original)
    {
        original.call();
        if (!IndustrialForegoingModule.ENABLE_IF_EIO_CAPACITOR_COMPAT) return;

        OME_Tweaks$capacitorStackHandler = CapacitorInventoryInjector.addCapacitorInventory((CustomElectricMachine)(Object)this);
    }
}
