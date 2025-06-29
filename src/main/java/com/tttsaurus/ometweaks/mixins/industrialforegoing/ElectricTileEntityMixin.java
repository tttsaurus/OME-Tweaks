package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.IEnergyStorageProvider;
import net.ndrei.teslacorelib.inventory.EnergyStorage;
import net.ndrei.teslacorelib.tileentities.ElectricTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ElectricTileEntity.class)
public class ElectricTileEntityMixin implements IEnergyStorageProvider
{
    @Shadow(remap = false)
    private EnergyStorage energyStorage;

    @Override
    public EnergyStorage get()
    {
        return energyStorage;
    }
}
