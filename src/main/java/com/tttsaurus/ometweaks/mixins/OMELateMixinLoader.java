package com.tttsaurus.ometweaks.mixins;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.OMETweaks;
import com.tttsaurus.ometweaks.integration.*;
import com.tttsaurus.ometweaks.integration._omefactory.OMEFactoryModule;
import com.tttsaurus.ometweaks.integration.extrautils2.ExtraUtils2Module;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import com.tttsaurus.ometweaks.integration.inworldcrafting.InWorldCraftingModule;
import com.tttsaurus.ometweaks.integration.jei.JEIModule;
import com.tttsaurus.ometweaks.integration.scp.SCPModule;
import com.tttsaurus.ometweaks.integration.taiga.TAIGAModule;
import com.tttsaurus.ometweaks.integration.thermalfoundation.ThermalFoundationModule;
import com.tttsaurus.ometweaks.integration.top.TOPModule;
import com.tttsaurus.ometweaks.utils.FileUtils;
import net.minecraftforge.common.config.Configuration;
import zone.rong.mixinbooter.ILateMixinLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OMELateMixinLoader implements ILateMixinLoader
{
    @Override
    public List<String> getMixinConfigs()
    {
        OMEConfig.CONFIG = new Configuration(FileUtils.makeFile("ometweaks.cfg"));
        OMEConfig.init();

        File modulesFile = FileUtils.getFile("modules.cfg");
        if (modulesFile.exists())
            OMETweaks.loadModules(modulesFile);

        OMETweaks.LOGGER.info("Mixin Stage");
        for (Map.Entry<OMETweaksModule, OMETweaksModuleSignature> entry: OMETweaks.MODULES.entrySet())
        {
            OMETweaksModule module = entry.getKey();
            OMETweaksModuleSignature annotation = entry.getValue();

            ConfigLoadingData data = OMETweaks.MODULE_CONFIGS.get(module);
            if (data != null && data.stages.contains(LoadingStage.MIXIN))
            {
                OMETweaks.LOGGER.info("Load configs for OME-Tweaks module [" + annotation.value() + "].");
                OMEConfig.invokeLoadConfig(module, data.loadConfigMethod, LoadingStage.MIXIN);
            }
        }

        List<String> list = new ArrayList<>();

        if (OMEConfig.ENABLE)
        {
            if (JEIModule.IS_MOD_LOADED && JEIModule.ENABLE_JEI_MODULE)
                list.add("mixins.ometweaks.jei.json");
            if (IndustrialForegoingModule.IS_MOD_LOADED && IndustrialForegoingModule.ENABLE_IF_MODULE)
                list.add("mixins.ometweaks.industrialforegoing.json");
            if (SCPModule.IS_MOD_LOADED && SCPModule.ENABLE_SCP_MODULE)
                list.add("mixins.ometweaks.scp.json");
            if (InWorldCraftingModule.IS_MOD_LOADED && InWorldCraftingModule.ENABLE_IWC_MODULE)
                list.add("mixins.ometweaks.inworldcrafting.json");
            if (ThermalFoundationModule.IS_MOD_LOADED && ThermalFoundationModule.ENABLE_TF_MODULE)
                list.add("mixins.ometweaks.thermalfoundation.json");
            if (ExtraUtils2Module.IS_MOD_LOADED && ExtraUtils2Module.ENABLE_XU2_MODULE)
                list.add("mixins.ometweaks.extrautils2.json");
            if (TAIGAModule.IS_MOD_LOADED && TAIGAModule.ENABLE_TAIGA_MODULE)
                list.add("mixins.ometweaks.taiga.json");
            if (TOPModule.IS_MOD_LOADED && TOPModule.ENABLE_TOP_MODULE)
                list.add("mixins.ometweaks.top.json");
        }

        if (OMEFactoryModule.ENABLE_OMEFACTORY_MODULE)
        {
            if (OMEFactoryModule.IS_TOP_LOADED && OMEFactoryModule.ENABLE_PLAYER_SAFE_ZONE && OMEFactoryModule.DISABLE_TOP_OUTSIDE_SAFE_ZONE)
                list.add("mixins.ometweaks._omefactory.top.json");
        }

        return list;
    }
}
