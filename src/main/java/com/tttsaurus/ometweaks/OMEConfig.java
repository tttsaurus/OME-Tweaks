package com.tttsaurus.ometweaks;

import com.tttsaurus.ometweaks.integration.enderio.GrindingBallData;
import com.tttsaurus.ometweaks.integration.industrialforegoing.AnimalRancherOutput;
import com.tttsaurus.ometweaks.integration.industrialforegoing.FuelDef;
import com.tttsaurus.ometweaks.integration.jei.CategoryModification;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
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
    public static boolean DISABLE_IF_FLUID_EFFECT_DRINKING_BIOFUEL;
    public static boolean DISABLE_IF_FLUID_EFFECT_DRINKING_SLUDGE;
    public static boolean DISABLE_IF_FLUID_EFFECT_DRINKING_SEWAGE;
    public static boolean DISABLE_IF_FLUID_EFFECT_DRINKING_MEAT;
    public static boolean DISABLE_IF_FLUID_EFFECT_DRINKING_PROTEIN;
    public static boolean DISABLE_IF_FLUID_EFFECT_DRINKING_LATEX;
    public static boolean DISABLE_IF_FLUID_EFFECT_BIOFUEL;
    public static boolean DISABLE_IF_FLUID_EFFECT_SLUDGE;
    public static boolean DISABLE_IF_FLUID_EFFECT_SEWAGE;
    public static boolean DISABLE_IF_FLUID_EFFECT_MEAT;
    public static boolean DISABLE_IF_FLUID_EFFECT_PROTEIN;
    public static boolean DISABLE_IF_FLUID_EFFECT_LATEX;
    public static boolean ENABLE_IF_CUSTOM_ANIMAL_RANCHER;
    public static boolean IF_CUSTOM_ANIMAL_RANCHER_FORTUNE;
    public final static Map<Class<? extends Entity>, AnimalRancherOutput> IF_CUSTOM_ANIMAL_RANCHER_RECIPES = new HashMap<>();
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

    //<editor-fold desc="enderio">
    public static boolean ENABLE_ENDERIO_MODULE;
    public static boolean ENABLE_ENDERIO_CUSTOM_CAPACITORS;
    public static final Map<String, Float> ENDERIO_CUSTOM_CAPACITORS = new HashMap<>();
    public static boolean ENABLE_ENDERIO_CUSTOM_GRINDING_BALLS;
    public static final Map<String, GrindingBallData> ENDERIO_CUSTOM_GRINDING_BALLS = new HashMap<>();
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
            String[] IF_PETRIFIED_FUEL_GENERATOR_FUELS = CONFIG.getStringList("Petrified Fuel Generator Fuel Def Override", "general.if.petrified_fuel_generator", new String[]{"minecraft:dirt,100,40"}, "A list of fuel definitions (Example: minecraft:dirt,100,40 so dirt generates 100 RF/tick for 40 ticks)\nConfig option \"burnTimeMultiplier\" from Industrial Foregoing still affects the duration you set\n");

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

            DISABLE_IF_FLUID_EFFECT_DRINKING_BIOFUEL = CONFIG.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.biofuel", false, "Disable Biofuel Potion Effect");
            DISABLE_IF_FLUID_EFFECT_BIOFUEL = CONFIG.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.biofuel", false, "Disable Biofuel Potion Effect");
            DISABLE_IF_FLUID_EFFECT_DRINKING_SLUDGE = CONFIG.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.sludge", false, "Disable Sludge Potion Effect");
            DISABLE_IF_FLUID_EFFECT_SLUDGE = CONFIG.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.sludge", false, "Disable Sludge Potion Effect");
            DISABLE_IF_FLUID_EFFECT_DRINKING_SEWAGE = CONFIG.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.sewage", false, "Disable Sewage Potion Effect");
            DISABLE_IF_FLUID_EFFECT_SEWAGE = CONFIG.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.sewage", false, "Disable Sewage Potion Effect");
            DISABLE_IF_FLUID_EFFECT_DRINKING_MEAT = CONFIG.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.meat", false, "Disable Meat Potion Effect");
            DISABLE_IF_FLUID_EFFECT_MEAT = CONFIG.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.meat", false, "Disable Meat Potion Effect");
            DISABLE_IF_FLUID_EFFECT_DRINKING_PROTEIN = CONFIG.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.protein", false, "Disable Protein Potion Effect");
            DISABLE_IF_FLUID_EFFECT_PROTEIN = CONFIG.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.protein", false, "Disable Protein Potion Effect");
            DISABLE_IF_FLUID_EFFECT_DRINKING_LATEX = CONFIG.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.latex", false, "Disable Latex Potion Effect");
            DISABLE_IF_FLUID_EFFECT_LATEX = CONFIG.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.latex", false, "Disable Latex Potion Effect");

            ENABLE_IF_CUSTOM_ANIMAL_RANCHER = CONFIG.getBoolean("Enable", "general.if.animal_rancher", false, "Enable Industrial Foregoing Custom Animal Rancher");
            IF_CUSTOM_ANIMAL_RANCHER_FORTUNE = CONFIG.getBoolean("Affected By Fortune", "general.if.animal_rancher", true, "Whether fortune addons work on those recipes");
            String[] IF_CUSTOM_ANIMAL_RANCHER_RECIPES = CONFIG.getStringList("Custom Animal Rancher Recipes", "general.if.animal_rancher", new String[]{"minecraft:zombie, water * 100, minecraft:apple * 2, 0.1"}, "A list of custom animal rancher recipes (entity registry name, fluid output, item output, chance)");

            OMEConfig.IF_CUSTOM_ANIMAL_RANCHER_RECIPES.clear();
            for (String arg: IF_CUSTOM_ANIMAL_RANCHER_RECIPES)
            {
                String[] args = arg.split(",");
                if (args.length != 4) continue;

                String entityRegistryName = args[0].trim();
                String fluidPart = args[1].trim();
                String itemPart = args[2].trim();
                String chancePart = args[3].trim();

                String[] fluidArgs = fluidPart.split("\\*");
                String[] itemArgs = itemPart.split("\\*");

                if (fluidArgs.length == 0 || fluidArgs.length > 2) continue;
                if (itemArgs.length == 0 || itemArgs.length > 2) continue;

                int fluidAmount = 1;
                if (fluidArgs.length == 2)
                {
                    try { fluidAmount = Integer.parseInt(fluidArgs[1].trim()); }
                    catch (NumberFormatException ignored) { continue; }
                }
                String fluidName = fluidArgs[0].trim();

                int itemCount = 1;
                if (itemArgs.length == 2)
                {
                    try { itemCount = Integer.parseInt(itemArgs[1].trim()); }
                    catch (NumberFormatException ignored) { continue; }
                }
                String[] itemNames = itemArgs[0].trim().split("@");
                if (itemNames.length == 0 || itemNames.length > 2) continue;
                String itemName = itemNames[0];
                int itemMeta = 0;
                if (itemNames.length == 2)
                    try { itemMeta = Integer.parseInt(itemNames[1]); }
                    catch (NumberFormatException ignored) { continue; }

                float chance = 0f;
                try { chance = Float.parseFloat(chancePart); }
                catch (NumberFormatException ignored) { continue; }

                EntityEntry entry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityRegistryName));
                if (entry == null) continue;

                Class<? extends Entity> key = entry.getEntityClass();
                AnimalRancherOutput value = new AnimalRancherOutput();

                Fluid fluid = FluidRegistry.getFluid(fluidName);
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

                if (fluid == null && item == null) continue;

                if (fluid != null)
                    value.fluidStack = new FluidStack(fluid, fluidAmount);
                if (item != null)
                    value.itemStack = new ItemStack(item, itemCount, itemMeta);

                value.chance = chance;

                OMEConfig.IF_CUSTOM_ANIMAL_RANCHER_RECIPES.put(key, value);
            }
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

            //<editor-fold desc="enderio config">
            ENABLE_ENDERIO_MODULE = CONFIG.getBoolean("Enable", "general.enderio", false, "Enable Ender IO Module / Whether mixins will be loaded");

            ENABLE_ENDERIO_CUSTOM_CAPACITORS = CONFIG.getBoolean("Enable", "general.enderio.capacitor", false, "Enable Custom Capacitors");
            String[] ENDERIO_CUSTOM_CAPACITORS = CONFIG.getStringList("Custom Capacitors", "general.enderio.capacitor", new String[]{"test,10"}, "A list of custom capacitors (Example: test,10 so 'test' is the name and '10' is the power of this capacitor)\nFormat: <name>,<power>\nNotice: <power> is a scaler and its range is [0, 10]\n\nItem registry name will be <name>_capacitor (e.g. test_capacitor)\nOredict will be capacitor<capitalized name> (e.g. capacitorTest)\nTranslation key will be item.ometweaks.<name>_capacitor.name (e.g. item.ometweaks.test_capacitor.name)\nModel location will be ometweaks:models/item/<name>_capacitor.json (see ometweaks:models/item/test_capacitor.json as an example)\nTexture location will be ometweaks:textures/items/<name>_capacitor.png (see ometweaks:textures/items/test_capacitor.png as an example)\n\n");

            OMEConfig.ENDERIO_CUSTOM_CAPACITORS.clear();
            for (String arg: ENDERIO_CUSTOM_CAPACITORS)
            {
                String[] args = arg.split(",");
                if (args.length != 2) continue;

                float power = 0f;
                try { power = Float.parseFloat(args[1].trim()); }
                catch (NumberFormatException e) { continue; }

                OMEConfig.ENDERIO_CUSTOM_CAPACITORS.put(args[0].trim(), power);
            }

            ENABLE_ENDERIO_CUSTOM_GRINDING_BALLS = CONFIG.getBoolean("Enable", "general.enderio.grinding_ball", false, "Enable Custom Grinding Balls");
            String[] ENDERIO_CUSTOM_GRINDING_BALLS = CONFIG.getStringList("Custom Grinding Balls", "general.enderio.grinding_ball", new String[]{"test,3,3,3,10000"}, "A list of custom grinding balls (Example: test,3,3,3,100)\nFormat: <name>,<main_output>,<bonus_output>,<power_use>,<durability>\n- <main_output> is a float (e.g. 1 = 100%)\n- <bonus_output> is a float (e.g. 1 = 100%)\n- <power_use> is a float (e.g. 1 = 100%)\n- <durability> is an int (e.g. 10000 is not a big value here)\n\nItem registry name will be <name>_grinding_ball (e.g. test_grinding_ball)\nOredict will be ball<capitalized name> (e.g. ballTest)\nTranslation key will be item.ometweaks.<name>_grinding_ball.name (e.g. item.ometweaks.test_grinding_ball.name)\nModel location will be ometweaks:models/item/<name>_grinding_ball.json (see ometweaks:models/item/test_grinding_ball.json as an example)\nTexture location will be ometweaks:textures/items/<name>_grinding_ball.png (see ometweaks:textures/items/test_grinding_ball.png as an example)\n\n");

            OMEConfig.ENDERIO_CUSTOM_GRINDING_BALLS.clear();
            for (String arg: ENDERIO_CUSTOM_GRINDING_BALLS)
            {
                String[] args = arg.split(",");
                if (args.length != 5) continue;

                GrindingBallData data = new GrindingBallData();

                try { data.grinding = Float.parseFloat(args[1].trim()); }
                catch (NumberFormatException e) { continue; }
                try { data.chance = Float.parseFloat(args[2].trim()); }
                catch (NumberFormatException e) { continue; }
                try { data.power = Float.parseFloat(args[3].trim()); }
                catch (NumberFormatException e) { continue; }
                try { data.durability = Integer.parseInt(args[4].trim()); }
                catch (NumberFormatException e) { continue; }

                String[] names = args[0].trim().split("_");
                StringBuilder builder = new StringBuilder();
                builder.append("ball");
                for (int i = 0; i < names.length; i++)
                    builder.append(String.valueOf(names[i].charAt(0)).toUpperCase(Locale.ROOT))
                            .append(names[i].substring(1));
                data.oreDict = builder.toString();

                OMEConfig.ENDERIO_CUSTOM_GRINDING_BALLS.put(args[0].trim(), data);
            }
            //</editor-fold>
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
