package com.tttsaurus.ometweaks.mixins;

import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MixinConfig implements ILateMixinLoader
{
    @Override
    public List<String> getMixinConfigs()
    {
        File file = new File("config/ometweaks.cfg");
        if (file.exists())
        {
            OMEConfig.CONFIG = new Configuration(file);
            OMEConfig.loadConfig();
        }

        List<String> list = new ArrayList<>();

        if (OMEConfig.ENABLE)
        {
            if (OMEConfig.ENABLE_JEI_MODULE && Loader.isModLoaded("jei"))
                list.add("mixins.ometweaks.jei.json");
            if (OMEConfig.ENABLE_IF_MODULE && Loader.isModLoaded("industrialforegoing"))
                list.add("mixins.ometweaks.industrialforegoing.json");
            if (OMEConfig.ENABLE_SCP_MODULE && Loader.isModLoaded("scp"))
                list.add("mixins.ometweaks.scp.json");
            if (OMEConfig.ENABLE_IWC_MODULE && Loader.isModLoaded("inworldcrafting"))
                list.add("mixins.ometweaks.inworldcrafting.json");
        }

        return list;
    }
}
