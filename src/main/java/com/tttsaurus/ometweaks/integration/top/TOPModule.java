package com.tttsaurus.ometweaks.integration.top;

import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

@OMETweaksModuleSignature("The One Probe")
public final class TOPModule extends OMETweaksModule
{
    public final static boolean IS_MOD_LOADED = Loader.isModLoaded("theoneprobe");

    public static boolean ENABLE_TOP_MODULE;
    public static boolean DISABLE_TOP_HUD_WHEN_GUI_SCREEN_IS_ON;

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        ENABLE_TOP_MODULE = config.getBoolean("Enable", "general.top", false, "Enable The One Probe Module / Whether mixins will be loaded");

        DISABLE_TOP_HUD_WHEN_GUI_SCREEN_IS_ON = config.getBoolean("Disable TOP HUD", "general.top.hud", false, "Disable TOP hud when a GuiScreen is on");
    }
}
