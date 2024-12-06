package com.tttsaurus.ometweaks.proxy;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.OMETweaks;
import com.tttsaurus.ometweaks.Tags;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        OMEConfig.CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        OMEConfig.loadConfig();
    }

    public void init(FMLInitializationEvent event)
    {
        OMETweaks.LOGGER.info(Tags.MODNAME + " starts initializing.");
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
