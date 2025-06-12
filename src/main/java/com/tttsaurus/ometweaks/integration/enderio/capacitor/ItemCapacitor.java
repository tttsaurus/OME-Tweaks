package com.tttsaurus.ometweaks.integration.enderio.capacitor;

import com.enderio.core.common.CompoundCapabilityProvider;
import crazypants.enderio.api.capacitor.CapabilityCapacitorData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCapacitor extends Item
{
    private final CapacitorData data;
    public ItemCapacitor(CapacitorData data)
    {
        this.data = data;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        ICapabilityProvider capProvider = new ICapabilityProvider()
        {
            @Override
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
            {
                return capability == CapabilityCapacitorData.getCapNN();
            }

            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
            {
                if (capability == CapabilityCapacitorData.getCapNN())
                    return CapabilityCapacitorData.getCapNN().cast(data);
                return null;
            }
        };
        return new CompoundCapabilityProvider(super.initCapabilities(stack, nbt), capProvider);
    }
}
