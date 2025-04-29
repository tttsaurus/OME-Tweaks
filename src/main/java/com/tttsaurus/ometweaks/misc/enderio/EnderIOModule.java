package com.tttsaurus.ometweaks.misc.enderio;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.misc.OMETweaksModule;
import com.tttsaurus.ometweaks.misc.OMETweaksModuleSignature;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import java.util.*;

@OMETweaksModuleSignature("Ender IO")
public final class EnderIOModule extends OMETweaksModule
{
    private static final Map<String, Item> items = new HashMap<>();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        if (!OMEConfig.ENABLE) return;
        if (!OMEConfig.ENABLE_ENDERIO_MODULE) return;

        if (OMEConfig.ENABLE_ENDERIO_CUSTOM_GRINDING_BALLS)
        {
            for (GrindingBallData data: OMEConfig.ENDERIO_CUSTOM_GRINDING_BALLS.values())
                EnderIOUtils.sendGrindingBallIMC(data.oreDict, data.grinding, data.chance, data.power, data.durability);
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (!OMEConfig.ENABLE) return;
        if (!OMEConfig.ENABLE_ENDERIO_MODULE) return;

        for (Map.Entry<String, Item> entry: items.entrySet())
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
                OreDictionary.registerOre(OMEConfig.ENDERIO_CUSTOM_GRINDING_BALLS.get(name).oreDict, entry.getValue());
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    public void registerModels(ModelRegistryEvent event)
    {
        if (!OMEConfig.ENABLE) return;
        if (!OMEConfig.ENABLE_ENDERIO_MODULE) return;

        for (Item item: items.values())
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        if (!OMEConfig.ENABLE) return;
        if (!OMEConfig.ENABLE_ENDERIO_MODULE) return;

        IForgeRegistry<Item> registry = event.getRegistry();

        if (OMEConfig.ENABLE_ENDERIO_CUSTOM_CAPACITORS)
        {
            for (Map.Entry<String, Float> entry: OMEConfig.ENDERIO_CUSTOM_CAPACITORS.entrySet())
                registerItem(registry, new ItemCapacitor(new CapacitorData(entry.getValue())), entry.getKey() + "_capacitor");
        }
        if (OMEConfig.ENABLE_ENDERIO_CUSTOM_GRINDING_BALLS)
        {
            for (String name: OMEConfig.ENDERIO_CUSTOM_GRINDING_BALLS.keySet())
                registerItem(registry, new Item(), name + "_grinding_ball");
        }
    }

    @SuppressWarnings("all")
    private void registerItem(IForgeRegistry<Item> registry, Item item, String name)
    {
        registry.register(item.setRegistryName(name).setTranslationKey(item.getRegistryName().toString().replace(':', '.')));
        items.put(name, item);
    }
}
