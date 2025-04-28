package com.tttsaurus.ometweaks.misc.industrialforegoing;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.misc.IOMETweaksModule;
import com.tttsaurus.ometweaks.misc.OMETweaksModule;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@OMETweaksModule("Industrial Foregoing")
public final class IndustrialForegoingModule implements IOMETweaksModule
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (Loader.isModLoaded("industrialforegoing"))
        {
            if (OMEConfig.ENABLE && OMEConfig.ENABLE_IF_MODULE && OMEConfig.ENABLE_IF_INFINITY_DRILL_BLACKLIST)
                MinecraftForge.EVENT_BUS.register(InfinityDrillBlacklist.class);
            if (OMEConfig.ENABLE && OMEConfig.ENABLE_IF_MODULE && OMEConfig.ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL)
                MinecraftForge.EVENT_BUS.register(InfinityDrillHarvestLevel.class);
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {

    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event)
    {

    }
}
