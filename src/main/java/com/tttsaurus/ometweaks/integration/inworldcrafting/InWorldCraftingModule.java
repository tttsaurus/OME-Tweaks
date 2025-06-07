package com.tttsaurus.ometweaks.integration.inworldcrafting;

import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraftforge.common.config.Configuration;

@OMETweaksModuleSignature("In World Crafting")
public final class InWorldCraftingModule extends OMETweaksModule
{
    public static boolean ENABLE_IWC_MODULE;
    public static boolean ENABLE_IWC_JEI_I18N;

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config)
    {
        ENABLE_IWC_MODULE = config.getBoolean("Enable", "general.inworldcrafting", false, "Enable In World Crafting Module / Whether mixins will be loaded");

        ENABLE_IWC_JEI_I18N = config.getBoolean("Enable", "general.inworldcrafting.jei", false, "Enable In World Crafting JEI I18n Support");
    }
}
