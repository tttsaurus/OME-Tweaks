package com.tttsaurus.ometweaks.integration.fluidintetweaker;

import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import com.tttsaurus.ometweaks.integration.fluidintetweaker.method.FITInternalMethods;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

@OMETweaksModuleSignature("Fluid Interaction Tweaker")
public final class FITModule extends OMETweaksModule
{
    public static FITInternalMethods internalMethods;
    public final static boolean IS_MOD_LOADED = Loader.isModLoaded("fluidintetweaker");

    @Override
    public void loadConfig(Configuration config, String currentStage)
    {

    }
}
