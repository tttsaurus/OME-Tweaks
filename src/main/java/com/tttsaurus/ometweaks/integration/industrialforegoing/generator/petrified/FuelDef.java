package com.tttsaurus.ometweaks.integration.industrialforegoing.generator.petrified;

public final class FuelDef
{
    // energy/tick
    public int rate;
    // number of ticks
    public int duration;

    public FuelDef(int rate, int duration)
    {
        this.rate = rate;
        this.duration = duration;
    }
}
