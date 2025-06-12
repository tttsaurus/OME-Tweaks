package com.tttsaurus.ometweaks.mixins;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.OMETweaks;
import com.tttsaurus.ometweaks.integration.*;
import com.tttsaurus.ometweaks.integration.extrautils2.ExtraUtils2Module;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import com.tttsaurus.ometweaks.integration.inworldcrafting.InWorldCraftingModule;
import com.tttsaurus.ometweaks.integration.jei.JEIModule;
import com.tttsaurus.ometweaks.integration.scp.SCPModule;
import com.tttsaurus.ometweaks.integration.thermalfoundation.ThermalFoundationModule;
import com.tttsaurus.ometweaks.utils.FileUtils;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
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
            if (JEIModule.ENABLE_JEI_MODULE && Loader.isModLoaded("jei"))
                list.add("mixins.ometweaks.jei.json");
            if (IndustrialForegoingModule.ENABLE_IF_MODULE && Loader.isModLoaded("industrialforegoing"))
                list.add("mixins.ometweaks.industrialforegoing.json");
            if (SCPModule.ENABLE_SCP_MODULE && Loader.isModLoaded("scp"))
                list.add("mixins.ometweaks.scp.json");
            if (InWorldCraftingModule.ENABLE_IWC_MODULE && Loader.isModLoaded("inworldcrafting"))
                list.add("mixins.ometweaks.inworldcrafting.json");
            if (ThermalFoundationModule.ENABLE_TF_MODULE && Loader.isModLoaded("thermalfoundation"))
                list.add("mixins.ometweaks.thermalfoundation.json");
            if (ExtraUtils2Module.ENABLE_XU2_MODULE && Loader.isModLoaded("extrautils2"))
                list.add("mixins.ometweaks.extrautils2.json");
        }

        return list;
    }
}
