package com.tttsaurus.ometweaks.integration.fluidintetweaker;

import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraftforge.fml.common.Loader;

@OMETweaksModuleSignature("Fluid Interaction Tweaker")
public final class FITModule extends OMETweaksModule
{
    public static FITInternalMethods internalMethods;
    public final static boolean isModLoaded = Loader.isModLoaded("fluidintetweaker");
}
