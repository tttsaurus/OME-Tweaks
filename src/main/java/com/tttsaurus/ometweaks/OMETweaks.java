package com.tttsaurus.ometweaks;

import com.tttsaurus.ometweaks.misc.OMETweaksModule;
import com.tttsaurus.ometweaks.misc.OMETweaksModuleSignature;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
@Mod(modid = Tags.MODID,
     version = Tags.VERSION,
     name = Tags.MODNAME,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required-after:mixinbooter@[10.0,)")
public class OMETweaks
{
    public static final Logger LOGGER = LogManager.getLogger(Tags.MODID);

    private static ASMDataTable asmDataTable;
    private static Map<OMETweaksModule, OMETweaksModuleSignature> modules = new HashMap<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);

        OMEConfig.CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        OMEConfig.loadConfig();

        asmDataTable = event.getAsmData();
        asmDataTable.getAll(OMETweaksModuleSignature.class.getCanonicalName()).forEach(data ->
        {
            String className = data.getClassName();
            try
            {
                Class<?> clazz = Class.forName(className);
                if (OMETweaksModule.class.isAssignableFrom(clazz))
                {
                    Class<? extends OMETweaksModule> moduleClass = clazz.asSubclass(OMETweaksModule.class);
                    try
                    {
                        OMETweaksModule module = moduleClass.newInstance();
                        modules.put(module, moduleClass.getAnnotation(OMETweaksModuleSignature.class));
                    }
                    catch (IllegalAccessException | InstantiationException e)
                    {
                        LOGGER.throwing(e);
                    }
                }
            }
            catch (ClassNotFoundException e)
            {
                LOGGER.throwing(e);
            }
        });
        LOGGER.info("OME-Tweaks modules instantiated.");

        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: modules.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            try { module.getClass().getDeclaredMethod("preInit", FMLPreInitializationEvent.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Calling preInit() of OME-Tweaks module '" + annotation.value() + "'.");
            module.preInit(event);
            LOGGER.info("Finished preInit() of OME-Tweaks module '" + annotation.value() + "'.");
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: modules.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            try { module.getClass().getDeclaredMethod("init", FMLInitializationEvent.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Calling init() of OME-Tweaks module '" + annotation.value() + "'.");
            module.init(event);
            LOGGER.info("Finished init() of OME-Tweaks module '" + annotation.value() + "'.");
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: modules.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            try { module.getClass().getDeclaredMethod("postInit", FMLPostInitializationEvent.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Calling postInit() of OME-Tweaks module '" + annotation.value() + "'.");
            module.postInit(event);
            LOGGER.info("Finished postInit() of OME-Tweaks module '" + annotation.value() + "'.");
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: modules.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            try { module.getClass().getDeclaredMethod("registerItems", RegistryEvent.Register.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Calling registerItems() of OME-Tweaks module '" + annotation.value() + "'.");
            module.registerItems(event);
            LOGGER.info("Finished registerItems() of OME-Tweaks module '" + annotation.value() + "'.");
        }
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event)
    {
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: modules.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            try { module.getClass().getDeclaredMethod("registerModels", ModelRegistryEvent.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Calling registerModels() of OME-Tweaks module '" + annotation.value() + "'.");
            module.registerModels(event);
            LOGGER.info("Finished registerModels() of OME-Tweaks module '" + annotation.value() + "'.");
        }
    }
}
