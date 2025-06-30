package com.tttsaurus.ometweaks.integration.scp;

import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

@OMETweaksModuleSignature("SCP")
public final class SCPModule extends OMETweaksModule
{
    public final static boolean IS_MOD_LOADED = Loader.isModLoaded("scp");

    public static boolean ENABLE_SCP_MODULE;
    public static boolean DISABLE_SCP_SLEEP_DEPRIVATION_CAP;
    public static boolean DISABLE_SCP_SHADOW_INFESTATION_CAP;
    public static boolean DISABLE_SCP_BLOODSTONE_CAP;
    public static boolean DISABLE_SCP_INFECTION_CAP;
    public static boolean DISABLE_SCP_KILLED_ENTITIES_CAP;
    public static boolean DISABLE_SCP_COWBELL_CAP;
    public static boolean DISABLE_SCP_LOST_ITEMS_CAP;

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        ENABLE_SCP_MODULE = config.getBoolean("Enable", "general.scp", false, "Enable SCP Lockdown Module / Whether mixins will be loaded");

        DISABLE_SCP_SLEEP_DEPRIVATION_CAP = config.getBoolean("Disable", "general.scp.capability.sleep_deprivation", false, "Disable SCP Sleep Deprivation Capability");
        DISABLE_SCP_SHADOW_INFESTATION_CAP = config.getBoolean("Disable", "general.scp.capability.shadow_infestation", false, "Disable SCP Shadow Infestation Capability");
        DISABLE_SCP_BLOODSTONE_CAP = config.getBoolean("Disable", "general.scp.capability.bloodstone", false, "Disable SCP Bloodstone Capability");
        DISABLE_SCP_INFECTION_CAP = config.getBoolean("Disable", "general.scp.capability.infection", false, "Disable SCP Infection Capability");
        DISABLE_SCP_KILLED_ENTITIES_CAP = config.getBoolean("Disable", "general.scp.capability.killed_entities", false, "Disable SCP Killed Entities Capability");
        DISABLE_SCP_COWBELL_CAP = config.getBoolean("Disable", "general.scp.capability.cowbell", false, "Disable SCP Cowbell Capability");
        DISABLE_SCP_LOST_ITEMS_CAP = config.getBoolean("Disable", "general.scp.capability.lost_items", false, "Disable SCP Lost Items Capability");
    }
}
