package com.tttsaurus.ometweaks.integration.industrialforegoing;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import java.util.*;

@OMETweaksModuleSignature("Industrial Foregoing")
public final class IndustrialForegoingModule extends OMETweaksModule
{
    public final static boolean isModLoaded = Loader.isModLoaded("industrialforegoing");

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

    @ConfigLoadingStage({LoadingStage.MIXIN, LoadingStage.POST_INIT})
    @Override
    public void loadConfig(Configuration config)
    {
        ENABLE_IF_MODULE = config.getBoolean("Enable", "general.if", false, "Enable Industrial Foregoing Module / Whether mixins will be loaded");

        ENABLE_IF_INFINITY_DRILL_BLACKLIST = config.getBoolean("Enable", "general.if.infinity_drill.blacklist", false, "Enable Industrial Foregoing Infinity Drill Blacklist");
        String[] IF_INFINITY_DRILL_BLACKLIST = config.getStringList("Infinity Drill Blacklist", "general.if.infinity_drill.blacklist", new String[]{}, "A list of block registry names that infinity drill cannot harvest (Example: minecraft:dirt@0 or ignore '@' like minecraft:dirt)");

        IndustrialForegoingModule.IF_INFINITY_DRILL_BLACKLIST.clear();
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
            IndustrialForegoingModule.IF_INFINITY_DRILL_BLACKLIST.add(itemStack);
        }

        ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL = config.getBoolean("Enable", "general.if.infinity_drill.harvest_level", false, "Enable Industrial Foregoing Infinity Drill Harvest Level");
        String[] IF_INFINITY_DRILL_HARVEST_LEVEL = config.getStringList("Infinity Drill Harvest Level", "general.if.infinity_drill.harvest_level", new String[]{"pickaxe:5", "shovel:5"}, "A list of harvest level specifications (Example: pickaxe:3)");

        IndustrialForegoingModule.IF_INFINITY_DRILL_HARVEST_LEVEL.clear();
        for (String arg: IF_INFINITY_DRILL_HARVEST_LEVEL)
        {
            String[] args = arg.split(":");
            if (args.length != 2) continue;
            int level = 0;
            try { level = Integer.parseInt(args[1]); }
            catch (NumberFormatException e) { continue; }
            IndustrialForegoingModule.IF_INFINITY_DRILL_HARVEST_LEVEL.put(args[0], level);
        }

        ENABLE_IF_PETRIFIED_FUEL_GENERATOR = config.getBoolean("Enable", "general.if.petrified_fuel_generator", false, "Enable Industrial Foregoing Petrified Fuel Generator Overhaul");
        String[] IF_PETRIFIED_FUEL_GENERATOR_FUELS = config.getStringList("Petrified Fuel Generator Fuel Def Override", "general.if.petrified_fuel_generator", new String[]{"minecraft:dirt,100,40"}, "A list of fuel definitions (Example: minecraft:dirt,100,40 so dirt generates 100 RF/tick for 40 ticks)\nConfig option \"burnTimeMultiplier\" from Industrial Foregoing still affects the duration you set\n");

        IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_FUELS.clear();
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

            IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_FUELS.put(itemStack, new FuelDef(rate, duration));
        }

        IF_PETRIFIED_FUEL_GENERATOR_JEI_OVERHAUL = config.getBoolean("Petrified Fuel Generator JEI Overhaul", "general.if.petrified_fuel_generator", false, "Whether to add burn time and i18n to the existing petrified fuel generator jei page");

        IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX = config.getInt("Petrified Fuel Generator Max Power", "general.if.petrified_fuel_generator", -1, -1, 0x7fffffff, "-1 for no max limit to all auto-added default fuels");
        IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX = config.getInt("Petrified Fuel Generator Max Burn Time", "general.if.petrified_fuel_generator", -1, -1, 0x7fffffff, "-1 for no max limit to all auto-added default fuels");

        DISABLE_IF_FLUID_EFFECT_DRINKING_BIOFUEL = config.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.biofuel", false, "Disable Biofuel Potion Effect");
        DISABLE_IF_FLUID_EFFECT_BIOFUEL = config.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.biofuel", false, "Disable Biofuel Potion Effect");
        DISABLE_IF_FLUID_EFFECT_DRINKING_SLUDGE = config.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.sludge", false, "Disable Sludge Potion Effect");
        DISABLE_IF_FLUID_EFFECT_SLUDGE = config.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.sludge", false, "Disable Sludge Potion Effect");
        DISABLE_IF_FLUID_EFFECT_DRINKING_SEWAGE = config.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.sewage", false, "Disable Sewage Potion Effect");
        DISABLE_IF_FLUID_EFFECT_SEWAGE = config.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.sewage", false, "Disable Sewage Potion Effect");
        DISABLE_IF_FLUID_EFFECT_DRINKING_MEAT = config.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.meat", false, "Disable Meat Potion Effect");
        DISABLE_IF_FLUID_EFFECT_MEAT = config.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.meat", false, "Disable Meat Potion Effect");
        DISABLE_IF_FLUID_EFFECT_DRINKING_PROTEIN = config.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.protein", false, "Disable Protein Potion Effect");
        DISABLE_IF_FLUID_EFFECT_PROTEIN = config.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.protein", false, "Disable Protein Potion Effect");
        DISABLE_IF_FLUID_EFFECT_DRINKING_LATEX = config.getBoolean("Disable Effect On Drink", "general.if.fluid_effect.latex", false, "Disable Latex Potion Effect");
        DISABLE_IF_FLUID_EFFECT_LATEX = config.getBoolean("Disable Fluid Tile Effect", "general.if.fluid_effect.latex", false, "Disable Latex Potion Effect");

        ENABLE_IF_CUSTOM_ANIMAL_RANCHER = config.getBoolean("Enable", "general.if.animal_rancher", false, "Enable Industrial Foregoing Custom Animal Rancher");
        IF_CUSTOM_ANIMAL_RANCHER_FORTUNE = config.getBoolean("Affected By Fortune", "general.if.animal_rancher", true, "Whether fortune addons work on those recipes");
        String[] IF_CUSTOM_ANIMAL_RANCHER_RECIPES = config.getStringList("Custom Animal Rancher Recipes", "general.if.animal_rancher", new String[]{"minecraft:zombie, water * 100, minecraft:apple * 2, 0.1"}, "A list of custom animal rancher recipes (entity registry name, fluid output, item output, chance)");

        IndustrialForegoingModule.IF_CUSTOM_ANIMAL_RANCHER_RECIPES.clear();
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

            IndustrialForegoingModule.IF_CUSTOM_ANIMAL_RANCHER_RECIPES.put(key, value);
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (isModLoaded)
        {
            if (OMEConfig.ENABLE && IndustrialForegoingModule.ENABLE_IF_MODULE && IndustrialForegoingModule.ENABLE_IF_INFINITY_DRILL_BLACKLIST)
                MinecraftForge.EVENT_BUS.register(InfinityDrillBlacklist.class);
            if (OMEConfig.ENABLE && IndustrialForegoingModule.ENABLE_IF_MODULE && IndustrialForegoingModule.ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL)
                MinecraftForge.EVENT_BUS.register(InfinityDrillHarvestLevel.class);
        }
    }
}
