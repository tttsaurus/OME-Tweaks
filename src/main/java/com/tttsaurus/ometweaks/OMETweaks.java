package com.tttsaurus.ometweaks;

import com.tttsaurus.ometweaks.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MODID,
     version = Tags.VERSION,
     name = Tags.MODNAME,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required-after:mixinbooter@[10.0,)"
)
public class OMETweaks
{
    public static final Logger LOGGER = LogManager.getLogger(Tags.MODID);

    @SidedProxy(
            clientSide = "com.tttsaurus.ometweaks.proxy.ClientProxy",
            serverSide = "com.tttsaurus.ometweaks.proxy.ServerProxy")
    private static CommonProxy proxy;

    public final static boolean IS_FLUIDINTETWEAKER_LOADED = Loader.isModLoaded("fluidintetweaker");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        LOGGER.info(Tags.MODNAME + " initialized.");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        
    }
}
