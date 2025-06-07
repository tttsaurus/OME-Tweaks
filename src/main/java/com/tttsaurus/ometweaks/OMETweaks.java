package com.tttsaurus.ometweaks;

import com.tttsaurus.ometweaks.integration.*;
import com.tttsaurus.ometweaks.utils.FileUtils;
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
import java.io.IOException;
import java.io.RandomAccessFile;
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

    public static final Map<OMETweaksModule, OMETweaksModuleSignature> MODULES = new HashMap<>();
    public static final Map<OMETweaksModule, ConfigLoadingData> MODULE_CONFIGS = new HashMap<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);

        ASMDataTable asmDataTable = event.getAsmData();

        if (OMEConfig.CONFIG == null)
        {
            OMEConfig.CONFIG = new Configuration(FileUtils.makeFile("ometweaks.cfg"));
            OMEConfig.init();
        }

        LOGGER.info("OME-Tweaks starts writing module classes to local.");
        try
        {
            RandomAccessFile raf = new RandomAccessFile(FileUtils.getFile("modules.cfg"), "rw");
            raf.setLength(0);
            raf.seek(0);

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
                            try
                            {
                                raf.writeBytes(module.getClass().getName() + "\n");
                            }
                            catch (IOException ignored) { }
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

            raf.close();
        }
        catch (IOException ignored) { }
        LOGGER.info("OME-Tweaks finished writing module classes to local.");

        LOGGER.info("Pre Init Stage");
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: MODULES.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            ConfigLoadingData data = MODULE_CONFIGS.get(module);
            if (data != null && data.stages.contains(LoadingStage.PRE_INIT))
            {
                LOGGER.info("Load configs for OME-Tweaks module [" + annotation.value() + "].");
                OMEConfig.invokeLoadConfig(module, data.loadConfigMethod);
            }

            try { module.getClass().getDeclaredMethod("preInit", FMLPreInitializationEvent.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Invoke preInit() of OME-Tweaks module [" + annotation.value() + "].");
            module.preInit(event);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        LOGGER.info("Init Stage");
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: MODULES.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            ConfigLoadingData data = MODULE_CONFIGS.get(module);
            if (data != null && data.stages.contains(LoadingStage.INIT))
            {
                LOGGER.info("Load configs for OME-Tweaks module [" + annotation.value() + "].");
                OMEConfig.invokeLoadConfig(module, data.loadConfigMethod);
            }

            try { module.getClass().getDeclaredMethod("init", FMLInitializationEvent.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Invoke init() of OME-Tweaks module [" + annotation.value() + "].");
            module.init(event);
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LOGGER.info("Post Init Stage");
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: MODULES.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            ConfigLoadingData data = MODULE_CONFIGS.get(module);
            if (data != null && data.stages.contains(LoadingStage.POST_INIT))
            {
                LOGGER.info("Load configs for OME-Tweaks module [" + annotation.value() + "].");
                OMEConfig.invokeLoadConfig(module, data.loadConfigMethod);
            }

            try { module.getClass().getDeclaredMethod("postInit", FMLPostInitializationEvent.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Invoke postInit() of OME-Tweaks module [" + annotation.value() + "].");
            module.postInit(event);
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        LOGGER.info("Register Items Stage");
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: MODULES.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            try { module.getClass().getDeclaredMethod("registerItems", RegistryEvent.Register.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Invoke registerItems() of OME-Tweaks module [" + annotation.value() + "].");
            module.registerItems(event);
        }
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event)
    {
        LOGGER.info("Register Models Stage");
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: MODULES.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            try { module.getClass().getDeclaredMethod("registerModels", ModelRegistryEvent.class); }
            catch (NoSuchMethodException | SecurityException e) { continue; }

            LOGGER.info("Invoke registerModels() of OME-Tweaks module [" + annotation.value() + "].");
            module.registerModels(event);
        }
    }
}
