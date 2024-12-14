package com.tttsaurus.ometweaks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import java.util.*;

@SuppressWarnings("all")
public final class OMEConfig
{
    public static boolean ENABLE;

    // jei
    public static boolean ENABLE_JEI_CATEGORY_ORDER;
    public static String[] JEI_CATEGORY_ORDER;

    // if
    public static boolean ENABLE_IF_INFINITY_DRILL_BLACKLIST;
    public final static List<ItemStack> IF_INFINITY_DRILL_BLACKLIST = new ArrayList<>();
    public static boolean ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL;
    public final static Map<String, Integer> IF_INFINITY_DRILL_HARVEST_LEVEL = new Hashtable<>();

    // scp
    public static boolean DISABLE_SCP_SLEEP_DEPRIVATION_CAP;
    public static boolean DISABLE_SCP_SHADOW_INFESTATION_CAP;
    public static boolean DISABLE_SCP_BLOODSTONE_CAP;
    public static boolean DISABLE_SCP_INFECTION_CAP;
    public static boolean DISABLE_SCP_KILLED_ENTITIES_CAP;
    public static boolean DISABLE_SCP_COWBELL_CAP;
    public static boolean DISABLE_SCP_LOST_ITEMS_CAP;

    public static Configuration CONFIG;

    public static void loadConfig()
    {
        try
        {
            CONFIG.load();

            ENABLE = CONFIG.getBoolean("Enable", "general", false, "Enable All OME Tweaks");

            //<editor-fold desc="jei mixins">
            ENABLE_JEI_CATEGORY_ORDER = CONFIG.getBoolean("Enable", "general.jei.category_order", false, "Enable JEI Category Order Mixin");
            JEI_CATEGORY_ORDER = CONFIG.getStringList("JEI Category Order", "general.jei.category_order", new String[]{}, "A list of jei category uids that determines the in-game jei displaying order");
            //</editor-fold>

            //<editor-fold desc="if mixins">
            ENABLE_IF_INFINITY_DRILL_BLACKLIST = CONFIG.getBoolean("Enable", "general.if.infinity_drill.blacklist", false, "Enable Industrial Foregoing Infinity Drill Blacklist Mixin");
            String[] IF_INFINITY_DRILL_BLACKLIST = CONFIG.getStringList("Infinity Drill Blacklist", "general.if.infinity_drill.blacklist", new String[]{}, "A list of block registry names that infinity drill cannot break (Example: minecraft:dirt@0 or ignore '@' like minecraft:dirt)");

            OMEConfig.IF_INFINITY_DRILL_BLACKLIST.clear();
            for (String arg : IF_INFINITY_DRILL_BLACKLIST)
            {
                String[] args = arg.split("@");
                if (args.length == 0 || args.length > 2) continue;
                int meta = 0;
                if (args.length == 2)
                    try { meta = Integer.parseInt(args[1]); }
                    catch (NumberFormatException e) { continue; }
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(args[0]));
                if (item == null) continue;
                ItemStack itemStack = new ItemStack(item, 1, meta);
                OMEConfig.IF_INFINITY_DRILL_BLACKLIST.add(itemStack);
            }

            ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL = CONFIG.getBoolean("Enable", "general.if.infinity_drill.harvest_level", false, "Enable Industrial Foregoing Infinity Drill Harvest Level Mixin");
            String[] IF_INFINITY_DRILL_HARVEST_LEVEL = CONFIG.getStringList("Infinity Drill Harvest Level", "general.if.infinity_drill.harvest_level", new String[]{"pickaxe:5", "shovel:5"}, "A list of harvest level specifications (Example: pickaxe:3)");

            OMEConfig.IF_INFINITY_DRILL_HARVEST_LEVEL.clear();
            for (String arg : IF_INFINITY_DRILL_HARVEST_LEVEL)
            {
                String[] args = arg.split(":");
                if (args.length != 2) continue;
                int level = 0;
                try { level = Integer.parseInt(args[1]); }
                catch (NumberFormatException e) { continue; }
                OMEConfig.IF_INFINITY_DRILL_HARVEST_LEVEL.put(args[0], level);
            }
            //</editor-fold>

            //<editor-fold desc="scp mixins">
            DISABLE_SCP_SLEEP_DEPRIVATION_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.sleep_deprivation", false, "Disable SCP Sleep Deprivation Capability");
            DISABLE_SCP_SHADOW_INFESTATION_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.shadow_infestation", false, "Disable SCP Shadow Infestation Capability");
            DISABLE_SCP_BLOODSTONE_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.bloodstone", false, "Disable SCP Bloodstone Capability");
            DISABLE_SCP_INFECTION_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.infection", false, "Disable SCP Infection Capability");
            DISABLE_SCP_KILLED_ENTITIES_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.killed_entities", false, "Disable SCP Killed Entities Capability");
            DISABLE_SCP_COWBELL_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.cowbell", false, "Disable SCP Cowbell Capability");
            DISABLE_SCP_LOST_ITEMS_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.lost_items", false, "Disable SCP Lost Items Capability");
            //</editor-fold>
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
