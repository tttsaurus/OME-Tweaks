package com.tttsaurus.ometweaks.integration.enderio;

import crazypants.enderio.api.capacitor.ICapacitorData;
import crazypants.enderio.api.capacitor.ICapacitorKey;
import javax.annotation.Nonnull;

public class CapacitorData implements ICapacitorData
{
    // 0 to 10
    private final float powerLevel;
    public CapacitorData(float powerLevel)
    {
        this.powerLevel = powerLevel;
    }

    @Override
    public float getUnscaledValue(@Nonnull ICapacitorKey iCapacitorKey)
    {
        return powerLevel;
    }

    @Override
    public @Nonnull String getUnlocalizedName()
    {
        return "";
    }

    @Override
    public @Nonnull String getLocalizedName()
    {
        return "";
    }
}
