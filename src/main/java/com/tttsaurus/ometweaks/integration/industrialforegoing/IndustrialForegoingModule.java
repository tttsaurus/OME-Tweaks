package com.tttsaurus.ometweaks.integration.industrialforegoing;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@OMETweaksModuleSignature("Industrial Foregoing")
public final class IndustrialForegoingModule extends OMETweaksModule
{
    public final static boolean isModLoaded = Loader.isModLoaded("industrialforegoing");

    @Override
    public void init(FMLInitializationEvent event)
    {
        if (isModLoaded)
        {
            if (OMEConfig.ENABLE && OMEConfig.ENABLE_IF_MODULE && OMEConfig.ENABLE_IF_INFINITY_DRILL_BLACKLIST)
                MinecraftForge.EVENT_BUS.register(InfinityDrillBlacklist.class);
            if (OMEConfig.ENABLE && OMEConfig.ENABLE_IF_MODULE && OMEConfig.ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL)
                MinecraftForge.EVENT_BUS.register(InfinityDrillHarvestLevel.class);
        }
    }
}
