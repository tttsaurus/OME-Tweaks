package com.tttsaurus.ometweaks.proxy;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.OMETweaks;
import com.tttsaurus.ometweaks.Tags;
import com.tttsaurus.ometweaks.api.fluidintetweaker.InternalMethods;
import com.tttsaurus.ometweaks.eventhandler.industrialforegoing.InfinityDrillBlacklist;
import com.tttsaurus.ometweaks.eventhandler.industrialforegoing.InfinityDrillHarvestLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
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

        if (Loader.isModLoaded("industrialforegoing"))
        {
            if (OMEConfig.ENABLE && OMEConfig.ENABLE_IF_MODULE && OMEConfig.ENABLE_IF_INFINITY_DRILL_BLACKLIST)
                MinecraftForge.EVENT_BUS.register(InfinityDrillBlacklist.class);
            if (OMEConfig.ENABLE && OMEConfig.ENABLE_IF_MODULE && OMEConfig.ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL)
                MinecraftForge.EVENT_BUS.register(InfinityDrillHarvestLevel.class);
        }

        if (OMETweaks.IS_FLUIDINTETWEAKER_LOADED)
        {
            InternalMethods.instance = new InternalMethods();
        }
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
