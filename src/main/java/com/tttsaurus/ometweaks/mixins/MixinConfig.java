package com.tttsaurus.ometweaks.mixins;

import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;
import java.util.ArrayList;
import java.util.List;

public class MixinConfig implements ILateMixinLoader
{
    @Override
    public List<String> getMixinConfigs()
    {
        List<String> list = new ArrayList<>();

        if (Loader.isModLoaded("jei"))
            list.add("mixins.ometweaks.jei.json");
        if (Loader.isModLoaded("industrialforegoing"))
            list.add("mixins.ometweaks.industrialforegoing.json");
        if (Loader.isModLoaded("scp"))
            list.add("mixins.ometweaks.scp.json");

        return list;
    }
}
