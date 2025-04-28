package com.tttsaurus.ometweaks.misc.enderio;

import com.tttsaurus.ometweaks.misc.IOMETweaksModule;
import com.tttsaurus.ometweaks.misc.OMETweaksModule;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@OMETweaksModule("Ender IO")
public final class EnderIOModule implements IOMETweaksModule
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        //EnderIOUtils.sendGrindingBallIMC();
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
