package com.tttsaurus.ometweaks.misc.fluidintetweaker;

import com.tttsaurus.ometweaks.misc.IOMETweaksModule;
import com.tttsaurus.ometweaks.misc.OMETweaksModule;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@OMETweaksModule("Fluid Interaction Tweaker")
public final class FITModule implements IOMETweaksModule
{
    public static FITInternalMethods internalMethods;
    public final static boolean isModLoaded = Loader.isModLoaded("fluidintetweaker");

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Override
    public void init(FMLInitializationEvent event)
    {

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
