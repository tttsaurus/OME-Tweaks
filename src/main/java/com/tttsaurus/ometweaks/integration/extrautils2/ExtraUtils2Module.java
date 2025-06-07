package com.tttsaurus.ometweaks.integration.extrautils2;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import com.tttsaurus.ometweaks.integration.fluidintetweaker.FITInternalMethods;
import com.tttsaurus.ometweaks.integration.fluidintetweaker.FITModule;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@OMETweaksModuleSignature("Extra Utils 2")
public final class ExtraUtils2Module extends OMETweaksModule
{
    public final static boolean isModLoaded = Loader.isModLoaded("extrautils2");

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (isModLoaded && OMEConfig.ENABLE && OMEConfig.ENABLE_XU2_MODULE && OMEConfig.ENABLE_XU2_NODE_MINING_FIT_COMPAT && FITModule.isModLoaded)
            FITModule.internalMethods = new FITInternalMethods();
    }
}
