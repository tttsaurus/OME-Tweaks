package com.tttsaurus.ometweaks;

import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import net.minecraftforge.common.config.Configuration;
import java.lang.reflect.Method;

public final class OMEConfig
{
    public static boolean ENABLE;

    public static Configuration CONFIG = null;

    public static void invokeLoadConfig(OMETweaksModule module, Method loadConfig)
    {
        try
        {
            loadConfig.invoke(module, CONFIG);
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

            ENABLE = CONFIG.getBoolean("Enable", "general", true, "Enable OME Tweaks");
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
