package com.tttsaurus.ometweaks;

import com.tttsaurus.ometweaks.gui.GuiResources;
import com.tttsaurus.ometweaks.integration.*;
import com.tttsaurus.ometweaks.render.RenderUtils;
import com.tttsaurus.ometweaks.utils.FileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("all")
@Mod(modid = Tags.MODID,
     version = Tags.VERSION,
     name = Tags.MODNAME,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required-after:mixinbooter@[10.0,)")
public class OMETweaks
{
    public static final Logger LOGGER = LogManager.getLogger(Tags.MODID);

    public static final Map<OMETweaksModule, OMETweaksModuleSignature> MODULES = new LinkedHashMap<>();
    public static final Map<OMETweaksModule, ConfigLoadingData> MODULE_CONFIGS = new LinkedHashMap<>();

    public static void loadModules(File modulesFile)
    {
        MODULES.clear();
        MODULE_CONFIGS.clear();
        try
        {
            RandomAccessFile raf = new RandomAccessFile(modulesFile, "rw");

            String line = raf.readLine();
            while (line != null)
            {
                if (!line.isEmpty())
                {
                    try
                    {
                        Class<?> clazz = Class.forName(line);
                        if (OMETweaksModule.class.isAssignableFrom(clazz))
                        {
                            Class<? extends OMETweaksModule> moduleClass = clazz.asSubclass(OMETweaksModule.class);

                            OMETweaksModule module = moduleClass.newInstance();
                            OMETweaksModuleSignature annotation = moduleClass.getAnnotation(OMETweaksModuleSignature.class);

                            MODULES.put(module, annotation);
                            Method method = module.getClass().getDeclaredMethod("loadConfig", Configuration.class, String.class);
                            if (method.isAnnotationPresent(ConfigLoadingStage.class))
                            {
                                ConfigLoadingStage stage = method.getAnnotation(ConfigLoadingStage.class);
                                MODULE_CONFIGS.put(module, new ConfigLoadingData(method, stage.value()));
                            }
                            LOGGER.info("OME-Tweaks module [" + annotation.value() + "] instantiated.");
                        }
                    }
                    catch (Exception ignored) { }
                }
                line = raf.readLine();
            }

            raf.close();
        }
        catch (IOException ignored) { }
    }

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

        File modulesFile = FileUtils.getFile("modules.cfg");
        if (!modulesFile.exists())
        {
            FileUtils.makeFile("[NOTICE] ometweaks config will be complete after the next run");
            LOGGER.info("First time of loading OME-Tweaks modules. Config file (/config/ometweaks/ometweaks.cfg) will be complete after the next run.");
        }
        else
        {
            File logFile = FileUtils.getFile("[NOTICE] ometweaks config will be complete after the next run");
            if (logFile.exists()) logFile.delete();
        }

        LOGGER.info("OME-Tweaks starts writing module classes to local.");
        try
        {
            RandomAccessFile raf = new RandomAccessFile(modulesFile, "rw");
            raf.setLength(0);
            raf.seek(0);

            List<String> classes = new ArrayList<>();
            asmDataTable.getAll(OMETweaksModuleSignature.class.getCanonicalName()).forEach(data ->
            {
                classes.add(data.getClassName());
            });

            Collections.sort(classes);
            for (String className: classes)
            {
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
            }

            raf.close();
        }
        catch (IOException ignored) { }
        LOGGER.info("OME-Tweaks finished writing module classes to local.");

        LOGGER.info("Pre Init Stage");

        if (FMLCommonHandler.instance().getSide().isClient())
        {
            GuiResources.init();
            LOGGER.info("GUI resources loaded.");
        }

        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: MODULES.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            ConfigLoadingData data = MODULE_CONFIGS.get(module);
            if (data != null && data.stages.contains(LoadingStage.PRE_INIT))
            {
                LOGGER.info("Load configs for OME-Tweaks module [" + annotation.value() + "].");
                OMEConfig.invokeLoadConfig(module, data.loadConfigMethod, LoadingStage.PRE_INIT);
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

        if (FMLCommonHandler.instance().getSide().isClient())
        {
            RenderUtils.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        }

        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: MODULES.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            ConfigLoadingData data = MODULE_CONFIGS.get(module);
            if (data != null && data.stages.contains(LoadingStage.INIT))
            {
                LOGGER.info("Load configs for OME-Tweaks module [" + annotation.value() + "].");
                OMEConfig.invokeLoadConfig(module, data.loadConfigMethod, LoadingStage.INIT);
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
                OMEConfig.invokeLoadConfig(module, data.loadConfigMethod, LoadingStage.POST_INIT);
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
