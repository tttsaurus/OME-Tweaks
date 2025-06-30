package com.tttsaurus.ometweaks.integration.thermalfoundation;

import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

@OMETweaksModuleSignature("Thermal Foundation")
public final class ThermalFoundationModule extends OMETweaksModule
{
    public final static boolean IS_MOD_LOADED = Loader.isModLoaded("thermalfoundation");

    public static boolean ENABLE_TF_MODULE;
    public static boolean DISABLE_TF_CRYOTHEUM_INTERACTIONS;
    public static boolean DISABLE_TF_MANA_INTERACTIONS;
    public static boolean DISABLE_TF_PETROTHEUM_INTERACTIONS;
    public static boolean DISABLE_TF_PYROTHEUM_INTERACTIONS;

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        ENABLE_TF_MODULE = config.getBoolean("Enable", "general.thermalfoundation", false, "Enable Thermal Foundation Module / Whether mixins will be loaded");
        DISABLE_TF_CRYOTHEUM_INTERACTIONS = config.getBoolean("Disable", "general.thermalfoundation.cryotheum.interactions", false, "Disable Thermal Foundation Cryotheum Interactions");
        DISABLE_TF_MANA_INTERACTIONS = config.getBoolean("Disable", "general.thermalfoundation.mana.interactions", false, "Disable Thermal Foundation Mana Interactions");
        DISABLE_TF_PETROTHEUM_INTERACTIONS = config.getBoolean("Disable", "general.thermalfoundation.petrotheum.interactions", false, "Disable Thermal Foundation Petrotheum Interactions");
        DISABLE_TF_PYROTHEUM_INTERACTIONS = config.getBoolean("Disable", "general.thermalfoundation.pyrotheum.interactions", false, "Disable Thermal Foundation Pyrotheum Interactions");
    }
}
