package com.tttsaurus.ometweaks.integration.taiga;

import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

@OMETweaksModuleSignature("TAIGA")
public class TAIGAModule extends OMETweaksModule
{
    public final static boolean IS_MOD_LOADED = Loader.isModLoaded("taiga");

    public static boolean ENABLE_TAIGA_MODULE;
    public static boolean DISABLE_TINKER_BOOK_TAIGA_PAGE;

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        ENABLE_TAIGA_MODULE = config.getBoolean("Enable", "general.taiga", false, "Enable TAIGA Module / Whether mixins will be loaded");

        DISABLE_TINKER_BOOK_TAIGA_PAGE = config.getBoolean("Disable", "general.taiga.book", false, "Disable Tinker Book TAIGA Page");
    }
}
