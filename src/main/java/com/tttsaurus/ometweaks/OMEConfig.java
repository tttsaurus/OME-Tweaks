package com.tttsaurus.ometweaks;

import com.tttsaurus.ometweaks.api.industrialforegoing.FuelDef;
import com.tttsaurus.ometweaks.api.jei.CategoryModification;
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

    //<editor-fold desc="jei">
    public static boolean ENABLE_JEI_MODULE;
    public static boolean ENABLE_JEI_CATEGORY_MODIFICATION;
    public final static Map<String, CategoryModification> JEI_CATEGORY_MODIFICATION = new HashMap<>();
    //</editor-fold>

    //<editor-fold desc="if">
    public static boolean ENABLE_IF_MODULE;
    public static boolean ENABLE_IF_INFINITY_DRILL_BLACKLIST;
    public final static List<ItemStack> IF_INFINITY_DRILL_BLACKLIST = new ArrayList<>();
    public static boolean ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL;
    public final static Map<String, Integer> IF_INFINITY_DRILL_HARVEST_LEVEL = new Hashtable<>();
    public static boolean ENABLE_IF_PETRIFIED_FUEL_GENERATOR;
    public final static Map<ItemStack, FuelDef> IF_PETRIFIED_FUEL_GENERATOR_FUELS = new HashMap<>();
    public static boolean IF_PETRIFIED_FUEL_GENERATOR_JEI_OVERHAUL;
    public static int IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX;
    public static int IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX;
    //</editor-fold>

    //<editor-fold desc="scp">
    public static boolean ENABLE_SCP_MODULE;
    public static boolean DISABLE_SCP_SLEEP_DEPRIVATION_CAP;
    public static boolean DISABLE_SCP_SHADOW_INFESTATION_CAP;
    public static boolean DISABLE_SCP_BLOODSTONE_CAP;
    public static boolean DISABLE_SCP_INFECTION_CAP;
    public static boolean DISABLE_SCP_KILLED_ENTITIES_CAP;
    public static boolean DISABLE_SCP_COWBELL_CAP;
    public static boolean DISABLE_SCP_LOST_ITEMS_CAP;
    //</editor-fold>

    //<editor-fold desc="iwc">
    public static boolean ENABLE_IWC_MODULE;
    public static boolean ENABLE_IWC_JEI_I18N;
    //</editor-fold>

    //<editor-fold desc="tf">
    public static boolean ENABLE_TF_MODULE;
    public static boolean DISABLE_TF_CRYOTHEUM_INTERACTIONS;
    public static boolean DISABLE_TF_MANA_INTERACTIONS;
    public static boolean DISABLE_TF_PETROTHEUM_INTERACTIONS;
    public static boolean DISABLE_TF_PYROTHEUM_INTERACTIONS;
    //</editor-fold>

    //<editor-fold desc="xu2">
    public static boolean ENABLE_XU2_MODULE;
    public static boolean ENABLE_XU2_NODE_MINING_FIT_COMPAT;
    //</editor-fold>

    public static Configuration CONFIG;

    public static void loadConfig()
    {
        try
        {
            CONFIG.load();

            ENABLE = CONFIG.getBoolean("Enable", "general", true, "Enable OME Tweaks");

            //<editor-fold desc="jei config">
            ENABLE_JEI_MODULE = CONFIG.getBoolean("Enable", "general.jei", false, "Enable JEI Module / Whether mixins will be loaded");

            ENABLE_JEI_CATEGORY_MODIFICATION = CONFIG.getBoolean("Enable", "general.jei.category_modification", false, "Enable JEI Category Modification");
            String[] JEI_CATEGORY_MODIFICATION = CONFIG.getStringList("JEI Category Modification", "general.jei.category_modification", new String[]{"tconstruct.alloy,[RL]ometweaks:textures/gui/jei/test.png", "tconstruct.smeltery,[Item]minecraft:apple@0"}, "A list of info that defines the modifications to the existing categories (Example: tconstruct.alloy,[RL]ometweaks:textures/gui/jei/test.png which changes the icon of tconstruct.alloy to ometweaks:textures/gui/jei/test.png)");

            OMEConfig.JEI_CATEGORY_MODIFICATION.clear();
            for (String arg: JEI_CATEGORY_MODIFICATION)
            {
                String[] args = arg.split(",");
                if (args.length != 2) continue;
                String key = args[0].trim();
                String rawValue = args[1].trim();
                CategoryModification value = new CategoryModification();

                if (rawValue.startsWith("[RL]"))
                    value.iconRL = new ResourceLocation(rawValue.substring(4).trim());
                else if (rawValue.startsWith("[Item]"))
                {
                    String itemRegistryName = rawValue.substring(6).trim();
                    String[] strs = itemRegistryName.split("@");
                    if (strs.length == 0 || strs.length > 2) continue;
                    int meta = 0;
                    if (strs.length == 2)
                        try { meta = Integer.parseInt(strs[1]); }
                        catch (NumberFormatException e) { continue; }
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(strs[0]));
                    if (item == null)
                    {
                        value.isGhostItem = true;
                        value.itemRegistryName = strs[0];
                        value.itemMeta = meta;
                    }
                    else
                    {
                        value.iconItem = new ItemStack(item, 1, meta);
                    }
                }
                else continue;

                OMEConfig.JEI_CATEGORY_MODIFICATION.put(key, value);
            }
            //</editor-fold>

            //<editor-fold desc="if config">
            ENABLE_IF_MODULE = CONFIG.getBoolean("Enable", "general.if", false, "Enable Industrial Foregoing Module / Whether mixins will be loaded");

            ENABLE_IF_INFINITY_DRILL_BLACKLIST = CONFIG.getBoolean("Enable", "general.if.infinity_drill.blacklist", false, "Enable Industrial Foregoing Infinity Drill Blacklist");
            String[] IF_INFINITY_DRILL_BLACKLIST = CONFIG.getStringList("Infinity Drill Blacklist", "general.if.infinity_drill.blacklist", new String[]{}, "A list of block registry names that infinity drill cannot harvest (Example: minecraft:dirt@0 or ignore '@' like minecraft:dirt)");

            OMEConfig.IF_INFINITY_DRILL_BLACKLIST.clear();
            for (String arg: IF_INFINITY_DRILL_BLACKLIST)
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

            ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL = CONFIG.getBoolean("Enable", "general.if.infinity_drill.harvest_level", false, "Enable Industrial Foregoing Infinity Drill Harvest Level");
            String[] IF_INFINITY_DRILL_HARVEST_LEVEL = CONFIG.getStringList("Infinity Drill Harvest Level", "general.if.infinity_drill.harvest_level", new String[]{"pickaxe:5", "shovel:5"}, "A list of harvest level specifications (Example: pickaxe:3)");

            OMEConfig.IF_INFINITY_DRILL_HARVEST_LEVEL.clear();
            for (String arg: IF_INFINITY_DRILL_HARVEST_LEVEL)
            {
                String[] args = arg.split(":");
                if (args.length != 2) continue;
                int level = 0;
                try { level = Integer.parseInt(args[1]); }
                catch (NumberFormatException e) { continue; }
                OMEConfig.IF_INFINITY_DRILL_HARVEST_LEVEL.put(args[0], level);
            }

            ENABLE_IF_PETRIFIED_FUEL_GENERATOR = CONFIG.getBoolean("Enable", "general.if.petrified_fuel_generator", false, "Enable Industrial Foregoing Petrified Fuel Generator Overhaul");
            String[] IF_PETRIFIED_FUEL_GENERATOR_FUELS = CONFIG.getStringList("Petrified Fuel Generator Fuel Def Override", "general.if.petrified_fuel_generator", new String[]{"minecraft:dirt,100,40"}, "A list of fuel definitions (Example: minecraft:dirt,100,40 so dirt generates 100 RF/tick for 40 ticks)\nConfig option \"burnTimeMultiplier\" from Industrial Foregoing still affect the duration you set\n");

            OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.clear();
            for (String arg: IF_PETRIFIED_FUEL_GENERATOR_FUELS)
            {
                String[] args = arg.split(",");
                if (args.length != 3) continue;
                String key = args[0].trim();
                String rawValue1 = args[1].trim();
                String rawValue2 = args[2].trim();

                String[] itemArgs = key.split("@");
                if (itemArgs.length == 0 || itemArgs.length > 2) continue;
                int meta = 0;
                if (itemArgs.length == 2)
                    try { meta = Integer.parseInt(itemArgs[1]); }
                    catch (NumberFormatException e) { continue; }
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemArgs[0]));
                if (item == null) continue;
                ItemStack itemStack = new ItemStack(item, 1, meta);

                int rate = 0;
                try { rate = Integer.parseInt(rawValue1); }
                catch (NumberFormatException e) { continue; }

                int duration = 0;
                try { duration = Integer.parseInt(rawValue2); }
                catch (NumberFormatException e) { continue; }

                OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.put(itemStack, new FuelDef(rate, duration));
            }

            IF_PETRIFIED_FUEL_GENERATOR_JEI_OVERHAUL = CONFIG.getBoolean("Petrified Fuel Generator JEI Overhaul", "general.if.petrified_fuel_generator", false, "Whether to add burn time and i18n to the existing petrified fuel generator jei page");

            IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX = CONFIG.getInt("Petrified Fuel Generator Max Power", "general.if.petrified_fuel_generator", -1, -1, 0x7fffffff, "-1 for no max limit to all auto-added default fuels");
            IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX = CONFIG.getInt("Petrified Fuel Generator Max Burn Time", "general.if.petrified_fuel_generator", -1, -1, 0x7fffffff, "-1 for no max limit to all auto-added default fuels");
            //</editor-fold>

            //<editor-fold desc="scp config">
            ENABLE_SCP_MODULE = CONFIG.getBoolean("Enable", "general.scp", false, "Enable SCP Lockdown Module / Whether mixins will be loaded");

            DISABLE_SCP_SLEEP_DEPRIVATION_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.sleep_deprivation", false, "Disable SCP Sleep Deprivation Capability");
            DISABLE_SCP_SHADOW_INFESTATION_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.shadow_infestation", false, "Disable SCP Shadow Infestation Capability");
            DISABLE_SCP_BLOODSTONE_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.bloodstone", false, "Disable SCP Bloodstone Capability");
            DISABLE_SCP_INFECTION_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.infection", false, "Disable SCP Infection Capability");
            DISABLE_SCP_KILLED_ENTITIES_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.killed_entities", false, "Disable SCP Killed Entities Capability");
            DISABLE_SCP_COWBELL_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.cowbell", false, "Disable SCP Cowbell Capability");
            DISABLE_SCP_LOST_ITEMS_CAP = CONFIG.getBoolean("Disable", "general.scp.capability.lost_items", false, "Disable SCP Lost Items Capability");
            //</editor-fold>

            //<editor-fold desc="iwc config">
            ENABLE_IWC_MODULE = CONFIG.getBoolean("Enable", "general.inworldcrafting", false, "Enable In World Crafting Module / Whether mixins will be loaded");

            ENABLE_IWC_JEI_I18N = CONFIG.getBoolean("Enable", "general.inworldcrafting.jei", false, "Enable In World Crafting JEI I18n Support");
            //</editor-fold>

            //<editor-fold desc="tf config">
            ENABLE_TF_MODULE = CONFIG.getBoolean("Enable", "general.thermalfoundation", false, "Enable Thermal Foundation Module / Whether mixins will be loaded");
            DISABLE_TF_CRYOTHEUM_INTERACTIONS = CONFIG.getBoolean("Disable", "general.thermalfoundation.cryotheum.interactions", false, "Disable Thermal Foundation Cryotheum Interactions");
            DISABLE_TF_MANA_INTERACTIONS = CONFIG.getBoolean("Disable", "general.thermalfoundation.mana.interactions", false, "Disable Thermal Foundation Mana Interactions");
            DISABLE_TF_PETROTHEUM_INTERACTIONS = CONFIG.getBoolean("Disable", "general.thermalfoundation.petrotheum.interactions", false, "Disable Thermal Foundation Petrotheum Interactions");
            DISABLE_TF_PYROTHEUM_INTERACTIONS = CONFIG.getBoolean("Disable", "general.thermalfoundation.pyrotheum.interactions", false, "Disable Thermal Foundation Pyrotheum Interactions");
            //</editor-fold>

            //<editor-fold desc="xu2 config">
            ENABLE_XU2_MODULE = CONFIG.getBoolean("Enable", "general.extrautils2", false, "Enable Extra Utilities Module / Whether mixins will be loaded");
            ENABLE_XU2_NODE_MINING_FIT_COMPAT = CONFIG.getBoolean("Enable", "general.extrautils2.node_mining_upgrade", false, "Enable Fluid Interaction Tweaker Compat with Mining Node");
            //</editor-fold>
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
