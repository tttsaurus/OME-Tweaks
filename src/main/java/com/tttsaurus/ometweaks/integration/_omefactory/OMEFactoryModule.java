package com.tttsaurus.ometweaks.integration._omefactory;

import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import com.tttsaurus.ometweaks.integration._omefactory.eventhandler.PlayerSafeZoneHandler;
import com.tttsaurus.ometweaks.integration._omefactory.eventhandler.PlayerSafeZoneHudHandler;
import com.tttsaurus.ometweaks.integration._omefactory.network.OMEFactoryNetwork;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import java.util.ArrayList;
import java.util.List;

@OMETweaksModuleSignature("OME Factory")
public class OMEFactoryModule extends OMETweaksModule
{
    public static final boolean IS_GAMESTAGES_LOADED = Loader.isModLoaded("gamestages");
    public static final boolean IS_TOP_LOADED = Loader.isModLoaded("theoneprobe");

    public static boolean ENABLE_OMEFACTORY_MODULE;

    public static boolean ENABLE_FORCE_SPAWN_BIOME;
    public static String INNER_BIOME_NAME;
    public static String OUTER_BIOME_NAME;
    public static int INNER_BIOME_RADIUS;
    public static int OUTER_BIOME_RADIUS;

    public static boolean ENABLE_PLAYER_SAFE_ZONE;
    public static int PLAYER_SAFE_ZONE_RADIUS;
    public static final List<PotionEffect> SAFE_ZONE_OUTSIDE_EFFECTS = new ArrayList<>();
    public static String GAMESTAGE_TO_DISABLE_SAFE_ZONE;
    public static boolean DISABLE_TOP_OUTSIDE_SAFE_ZONE;

    private String[] RAW_SAFE_ZONE_OUTSIDE_EFFECTS;

    @ConfigLoadingStage({LoadingStage.MIXIN, LoadingStage.POST_INIT})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        if (currentStage.equals(LoadingStage.MIXIN))
        {
            ENABLE_OMEFACTORY_MODULE = config.getBoolean("Enable", "omefactory", false, "This whole module is created for the modpack Oh My Enigma\nOptions here aren't related to \"general\" category");

            ENABLE_FORCE_SPAWN_BIOME = config.getBoolean("Enable Forced Spawn Biome", "omefactory.spawn_biome", true, "Whether to force a spawn biome");
            INNER_BIOME_NAME = config.getString("Inner Biome Registry Name", "omefactory.spawn_biome", "minecraft:desert", "The registry name of the spawn area inner biome");
            OUTER_BIOME_NAME = config.getString("Outer Biome Registry Name", "omefactory.spawn_biome", "minecraft:ocean", "The registry name of the spawn area outer biome");
            INNER_BIOME_RADIUS = config.getInt("Inner Biome Radius", "omefactory.spawn_biome", 64, 16, 0x7fffffff, "The inner biome radius");
            OUTER_BIOME_RADIUS = config.getInt("Outer Biome Radius", "omefactory.spawn_biome", 128, 32, 0x7fffffff, "The outer biome radius");

            ENABLE_PLAYER_SAFE_ZONE = config.getBoolean("Enable Player Safe Zone", "omefactory.safe_zone", true, "Whether to create a restrictive safe zone");
            PLAYER_SAFE_ZONE_RADIUS = config.getInt("Safe Zone Radius", "omefactory.safe_zone", 64, 16, 0x7fffffff, "The safe zone radius");
            RAW_SAFE_ZONE_OUTSIDE_EFFECTS = config.getStringList("Safe Zone Outside Effects", "omefactory.safe_zone", new String[0], "The potion effects applied to player when leaving the safe zone");
            GAMESTAGE_TO_DISABLE_SAFE_ZONE = config.getString("Gamestage That Disables Safe Zone", "omefactory.safe_zone", "", "The gamestage that disables safe zone");
            DISABLE_TOP_OUTSIDE_SAFE_ZONE = config.getBoolean("Disable TOP Hud Outside Safe Zone", "omefactory.safe_zone", false, "Whether to disable TOP hud outside the safe zone");
        }

        if (currentStage.equals(LoadingStage.POST_INIT))
        {
            //<editor-fold desc="parse safe zone potion effects">
            SAFE_ZONE_OUTSIDE_EFFECTS.clear();
            for (String arg: RAW_SAFE_ZONE_OUTSIDE_EFFECTS)
            {
                Potion potion = Potion.getPotionFromResourceLocation(arg);
                if (potion == null) continue;
                SAFE_ZONE_OUTSIDE_EFFECTS.add(new PotionEffect(potion, 20, 0));
            }
            //</editor-fold>
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (ENABLE_OMEFACTORY_MODULE && ENABLE_PLAYER_SAFE_ZONE)
        {
            MinecraftForge.EVENT_BUS.register(PlayerSafeZoneHandler.class);
            if (FMLCommonHandler.instance().getSide().isClient())
            {
                MinecraftForge.EVENT_BUS.register(PlayerSafeZoneHudHandler.class);
            }
        }

        OMEFactoryNetwork.init();
    }
}
