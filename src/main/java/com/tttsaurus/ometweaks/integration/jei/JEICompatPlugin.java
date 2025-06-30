package com.tttsaurus.ometweaks.integration.jei;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
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
        if (JEIModule.IS_MOD_LOADED &&
                IndustrialForegoingModule.IS_MOD_LOADED &&
                OMEConfig.ENABLE &&
                IndustrialForegoingModule.ENABLE_IF_MODULE &&
                IndustrialForegoingModule.ENABLE_IF_MACHINE_JEI_EXCLUSION)
        {
            registry.addAdvancedGuiHandlers(new MachineJEIExclusionHandler());
        }
    }
}
