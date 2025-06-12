package com.tttsaurus.ometweaks.integration.enderio;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import com.tttsaurus.ometweaks.integration.enderio.capacitor.CapacitorData;
import com.tttsaurus.ometweaks.integration.enderio.capacitor.ItemCapacitor;
import com.tttsaurus.ometweaks.integration.enderio.grindingball.GrindingBallData;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import java.util.*;

@OMETweaksModuleSignature("Ender IO")
public final class EnderIOModule extends OMETweaksModule
{
    private static final Map<String, Item> ITEMS = new HashMap<>();
    public static final boolean IS_MOD_LOADED = Loader.isModLoaded("enderio");

    public static boolean ENABLE_ENDERIO_MODULE;
    public static boolean ENABLE_ENDERIO_CUSTOM_CAPACITORS;
    public static final Map<String, Float> ENDERIO_CUSTOM_CAPACITORS = new HashMap<>();
    public static boolean ENABLE_ENDERIO_CUSTOM_GRINDING_BALLS;
    public static final Map<String, GrindingBallData> ENDERIO_CUSTOM_GRINDING_BALLS = new HashMap<>();

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        ENABLE_ENDERIO_MODULE = config.getBoolean("Enable", "general.enderio", false, "Enable Ender IO Module / Whether mixins will be loaded");

        ENABLE_ENDERIO_CUSTOM_CAPACITORS = config.getBoolean("Enable", "general.enderio.capacitor", false, "Enable Custom Capacitors");
        String[] ENDERIO_CUSTOM_CAPACITORS = config.getStringList("Custom Capacitors", "general.enderio.capacitor", new String[]{"test,10"}, "A list of custom capacitors (Example: test,10 so 'test' is the name and '10' is the power of this capacitor)\nFormat: <name>,<power>\nNotice: <power> is a scaler and its range is [0, 10]\n\nItem registry name will be <name>_capacitor (e.g. test_capacitor)\nOredict will be capacitor<capitalized name> (e.g. capacitorTest)\nTranslation key will be item.ometweaks.<name>_capacitor.name (e.g. item.ometweaks.test_capacitor.name)\nModel location will be ometweaks:models/item/<name>_capacitor.json (see ometweaks:models/item/test_capacitor.json as an example)\nTexture location will be ometweaks:textures/items/<name>_capacitor.png (see ometweaks:textures/items/test_capacitor.png as an example)\n\n");

        EnderIOModule.ENDERIO_CUSTOM_CAPACITORS.clear();
        for (String arg: ENDERIO_CUSTOM_CAPACITORS)
        {
            String[] args = arg.split(",");
            if (args.length != 2) continue;

            float power = 0f;
            try { power = Float.parseFloat(args[1].trim()); }
            catch (NumberFormatException e) { continue; }

            EnderIOModule.ENDERIO_CUSTOM_CAPACITORS.put(args[0].trim(), power);
        }

        ENABLE_ENDERIO_CUSTOM_GRINDING_BALLS = config.getBoolean("Enable", "general.enderio.grinding_ball", false, "Enable Custom Grinding Balls");
        String[] ENDERIO_CUSTOM_GRINDING_BALLS = config.getStringList("Custom Grinding Balls", "general.enderio.grinding_ball", new String[]{"test,3,3,3,10000"}, "A list of custom grinding balls (Example: test,3,3,3,100)\nFormat: <name>,<main_output>,<bonus_output>,<power_use>,<durability>\n- <main_output> is a float (e.g. 1 = 100%)\n- <bonus_output> is a float (e.g. 1 = 100%)\n- <power_use> is a float (e.g. 1 = 100%)\n- <durability> is an int (e.g. 10000 is not a big value here)\n\nItem registry name will be <name>_grinding_ball (e.g. test_grinding_ball)\nOredict will be ball<capitalized name> (e.g. ballTest)\nTranslation key will be item.ometweaks.<name>_grinding_ball.name (e.g. item.ometweaks.test_grinding_ball.name)\nModel location will be ometweaks:models/item/<name>_grinding_ball.json (see ometweaks:models/item/test_grinding_ball.json as an example)\nTexture location will be ometweaks:textures/items/<name>_grinding_ball.png (see ometweaks:textures/items/test_grinding_ball.png as an example)\n\n");

        EnderIOModule.ENDERIO_CUSTOM_GRINDING_BALLS.clear();
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

            EnderIOModule.ENDERIO_CUSTOM_GRINDING_BALLS.put(args[0].trim(), data);
        }
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        if (!OMEConfig.ENABLE) return;
        if (!EnderIOModule.ENABLE_ENDERIO_MODULE) return;
        if (!IS_MOD_LOADED) return;

        if (EnderIOModule.ENABLE_ENDERIO_CUSTOM_GRINDING_BALLS)
        {
            for (GrindingBallData data: EnderIOModule.ENDERIO_CUSTOM_GRINDING_BALLS.values())
                EnderIOUtils.sendGrindingBallIMC(data.oreDict, data.grinding, data.chance, data.power, data.durability);
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (!OMEConfig.ENABLE) return;
        if (!EnderIOModule.ENABLE_ENDERIO_MODULE) return;
        if (!IS_MOD_LOADED) return;

        for (Map.Entry<String, Item> entry: ITEMS.entrySet())
        {
            String[] args = entry.getKey().split("_");

            if (args.length >= 2 && args[args.length - 1].equals("capacitor"))
            {
                StringBuilder builder = new StringBuilder();
                builder.append("capacitor");
                for (int i = 0; i < args.length - 1; i++)
                    builder.append(String.valueOf(args[i].charAt(0)).toUpperCase(Locale.ROOT))
                            .append(args[i].substring(1));
                OreDictionary.registerOre(builder.toString(), entry.getValue());
            }
            else if (args.length >= 3 && args[args.length - 2].equals("grinding") && args[args.length - 1].equals("ball"))
            {
                String name = String.join("_", Arrays.copyOf(args, args.length - 2));
                OreDictionary.registerOre(EnderIOModule.ENDERIO_CUSTOM_GRINDING_BALLS.get(name).oreDict, entry.getValue());
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    public void registerModels(ModelRegistryEvent event)
    {
        if (!OMEConfig.ENABLE) return;
        if (!EnderIOModule.ENABLE_ENDERIO_MODULE) return;
        if (!IS_MOD_LOADED) return;

        for (Item item: ITEMS.values())
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        if (!OMEConfig.ENABLE) return;
        if (!EnderIOModule.ENABLE_ENDERIO_MODULE) return;
        if (!IS_MOD_LOADED) return;

        IForgeRegistry<Item> registry = event.getRegistry();

        if (EnderIOModule.ENABLE_ENDERIO_CUSTOM_CAPACITORS)
        {
            for (Map.Entry<String, Float> entry: EnderIOModule.ENDERIO_CUSTOM_CAPACITORS.entrySet())
                registerItem(registry, new ItemCapacitor(new CapacitorData(entry.getValue())), entry.getKey() + "_capacitor");
        }
        if (EnderIOModule.ENABLE_ENDERIO_CUSTOM_GRINDING_BALLS)
        {
            for (String name: EnderIOModule.ENDERIO_CUSTOM_GRINDING_BALLS.keySet())
                registerItem(registry, new Item(), name + "_grinding_ball");
        }
    }

    @SuppressWarnings("all")
    private void registerItem(IForgeRegistry<Item> registry, Item item, String name)
    {
        registry.register(item.setRegistryName(name).setTranslationKey(item.getRegistryName().toString().replace(':', '.')));
        ITEMS.put(name, item);
    }
}
