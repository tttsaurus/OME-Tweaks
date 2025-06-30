package com.tttsaurus.ometweaks.integration.jei;

import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.jei.MachineJEIExclusionHandler;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import javax.annotation.Nonnull;

@JEIPlugin
public class JEICompatPlugin implements IModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registry.addAdvancedGuiHandlers(new MachineJEIExclusionHandler());
    }
}
