package com.tttsaurus.ometweaks.integration.extrautils2;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import com.tttsaurus.ometweaks.integration.fluidintetweaker.method.FITInternalMethods;
import com.tttsaurus.ometweaks.integration.fluidintetweaker.FITModule;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@OMETweaksModuleSignature("Extra Utils 2")
public final class ExtraUtils2Module extends OMETweaksModule
{
    public final static boolean IS_MOD_LOADED = Loader.isModLoaded("extrautils2");

    public static boolean ENABLE_XU2_MODULE;
    public static boolean ENABLE_XU2_NODE_MINING_FIT_COMPAT;

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        ENABLE_XU2_MODULE = config.getBoolean("Enable", "general.extrautils2", false, "Enable Extra Utilities Module / Whether mixins will be loaded");
        ENABLE_XU2_NODE_MINING_FIT_COMPAT = config.getBoolean("Enable", "general.extrautils2.node_mining_upgrade", false, "Enable Fluid Interaction Tweaker Compat with Mining Node");
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (IS_MOD_LOADED && OMEConfig.ENABLE && ExtraUtils2Module.ENABLE_XU2_MODULE && ExtraUtils2Module.ENABLE_XU2_NODE_MINING_FIT_COMPAT && FITModule.IS_MOD_LOADED)
            FITModule.internalMethods = new FITInternalMethods();
    }
}
