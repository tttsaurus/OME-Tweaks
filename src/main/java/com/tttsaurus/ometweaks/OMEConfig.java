package com.tttsaurus.ometweaks;

import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import net.minecraftforge.common.config.Configuration;
import java.lang.reflect.Method;

public final class OMEConfig
{
    public static boolean ENABLE;

    public static Configuration CONFIG = null;

    public static void invokeLoadConfig(OMETweaksModule module, Method loadConfig, String stage)
    {
        try
        {
            loadConfig.invoke(module, CONFIG, stage);
        }
        catch (Throwable ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }

    public static void init()
    {
        try
        {
            CONFIG.load();

            ENABLE = CONFIG.getBoolean("Enable", "general", true, "Enable OME Tweaks\nThis is a toggle for all tweaks under \"general\" category");
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
