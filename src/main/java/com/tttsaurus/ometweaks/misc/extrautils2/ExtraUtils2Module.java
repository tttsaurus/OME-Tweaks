package com.tttsaurus.ometweaks.misc.extrautils2;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.misc.IOMETweaksModule;
import com.tttsaurus.ometweaks.misc.OMETweaksModule;
import com.tttsaurus.ometweaks.misc.fluidintetweaker.FITInternalMethods;
import com.tttsaurus.ometweaks.misc.fluidintetweaker.FITModule;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@OMETweaksModule("Extra Utils 2")
public final class ExtraUtils2Module implements IOMETweaksModule
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (OMEConfig.ENABLE && OMEConfig.ENABLE_XU2_MODULE && OMEConfig.ENABLE_XU2_NODE_MINING_FIT_COMPAT && FITModule.isModLoaded)
            FITModule.internalMethods = new FITInternalMethods();
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
