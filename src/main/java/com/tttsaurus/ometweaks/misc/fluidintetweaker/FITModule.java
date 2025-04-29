package com.tttsaurus.ometweaks.misc.fluidintetweaker;

import com.tttsaurus.ometweaks.misc.OMETweaksModule;
import com.tttsaurus.ometweaks.misc.OMETweaksModuleSignature;
import net.minecraftforge.fml.common.Loader;

@OMETweaksModuleSignature("Fluid Interaction Tweaker")
public final class FITModule extends OMETweaksModule
{
    public static FITInternalMethods internalMethods;
    public final static boolean isModLoaded = Loader.isModLoaded("fluidintetweaker");
}
