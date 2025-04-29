package com.tttsaurus.ometweaks.misc.extrautils2;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.misc.OMETweaksModule;
import com.tttsaurus.ometweaks.misc.OMETweaksModuleSignature;
import com.tttsaurus.ometweaks.misc.fluidintetweaker.FITInternalMethods;
import com.tttsaurus.ometweaks.misc.fluidintetweaker.FITModule;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@OMETweaksModuleSignature("Extra Utils 2")
public final class ExtraUtils2Module extends OMETweaksModule
{
    @Override
    public void init(FMLInitializationEvent event)
    {
        if (OMEConfig.ENABLE && OMEConfig.ENABLE_XU2_MODULE && OMEConfig.ENABLE_XU2_NODE_MINING_FIT_COMPAT && FITModule.isModLoaded)
            FITModule.internalMethods = new FITInternalMethods();
    }
}
