package com.tttsaurus.ometweaks.integration._omefactory;

import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraftforge.common.config.Configuration;

@OMETweaksModuleSignature("OME Factory")
public class OMEFactoryModule extends OMETweaksModule
{
    public static boolean ENABLE_OMEFACTORY_MODULE;

    public static boolean ENABLE_FORCE_SPAWN_BIOME;
    public static String INNER_BIOME_NAME;
    public static String OUTER_BIOME_NAME;
    public static int INNER_BIOME_RADIUS;
    public static int OUTER_BIOME_RADIUS;

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        ENABLE_OMEFACTORY_MODULE = config.getBoolean("Enable", "omefactory", false, "This whole module is created for the modpack Oh My Enigma\nOptions here aren't related to \"general\" category");

        ENABLE_FORCE_SPAWN_BIOME = config.getBoolean("Enable Forced Spawn Biome", "omefactory.spawn_biome", true, "Whether to force a spawn biome");
        INNER_BIOME_NAME = config.getString("Inner Biome Registry Name", "omefactory.spawn_biome", "minecraft:desert", "The registry name of the spawn area inner biome");
        OUTER_BIOME_NAME = config.getString("Outer Biome Registry Name", "omefactory.spawn_biome", "minecraft:ocean", "The registry name of the spawn area outer biome");
        INNER_BIOME_RADIUS = config.getInt("Inner Biome Radius", "omefactory.spawn_biome", 64, 16, 0x7fffffff, "The inner biome radius");
        OUTER_BIOME_RADIUS = config.getInt("Outer Biome Radius", "omefactory.spawn_biome", 128, 32, 0x7fffffff, "The outer biome radius");
    }
}
