package com.tttsaurus.ometweaks.integration.jei;

import com.tttsaurus.ometweaks.integration.ConfigLoadingStage;
import com.tttsaurus.ometweaks.integration.LoadingStage;
import com.tttsaurus.ometweaks.integration.OMETweaksModule;
import com.tttsaurus.ometweaks.integration.OMETweaksModuleSignature;
import com.tttsaurus.ometweaks.integration.jei.category.CategoryModification;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import java.util.HashMap;
import java.util.Map;

@OMETweaksModuleSignature("JEI")
public final class JEIModule extends OMETweaksModule
{
    public static boolean ENABLE_JEI_MODULE;
    public static boolean ENABLE_JEI_CATEGORY_MODIFICATION;
    public final static Map<String, CategoryModification> JEI_CATEGORY_MODIFICATION = new HashMap<>();

    @ConfigLoadingStage({LoadingStage.MIXIN})
    @Override
    public void loadConfig(Configuration config, String currentStage)
    {
        ENABLE_JEI_MODULE = config.getBoolean("Enable", "general.jei", false, "Enable JEI Module / Whether mixins will be loaded");

        ENABLE_JEI_CATEGORY_MODIFICATION = config.getBoolean("Enable", "general.jei.category_modification", false, "Enable JEI Category Modification");
        String[] JEI_CATEGORY_MODIFICATION = config.getStringList("JEI Category Modification", "general.jei.category_modification", new String[]{"tconstruct.alloy,[RL]ometweaks:textures/gui/jei/test.png", "tconstruct.smeltery,[Item]minecraft:apple@0"}, "A list of info that defines the modifications to the existing categories (Example: tconstruct.alloy,[RL]ometweaks:textures/gui/jei/test.png which changes the icon of tconstruct.alloy to ometweaks:textures/gui/jei/test.png)");

        JEIModule.JEI_CATEGORY_MODIFICATION.clear();
        for (String arg: JEI_CATEGORY_MODIFICATION)
        {
            String[] args = arg.split(",");
            if (args.length != 2) continue;
            String key = args[0].trim();
            String rawValue = args[1].trim();
            CategoryModification value = new CategoryModification();

            if (rawValue.startsWith("[RL]"))
                value.iconRL = new ResourceLocation(rawValue.substring(4).trim());
            else if (rawValue.startsWith("[Item]"))
            {
                String itemRegistryName = rawValue.substring(6).trim();
                String[] strs = itemRegistryName.split("@");
                if (strs.length == 0 || strs.length > 2) continue;
                int meta = 0;
                if (strs.length == 2)
                    try { meta = Integer.parseInt(strs[1]); }
                    catch (NumberFormatException e) { continue; }
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(strs[0]));
                if (item == null)
                {
                    value.isGhostItem = true;
                    value.itemRegistryName = strs[0];
                    value.itemMeta = meta;
                }
                else
                {
                    value.iconItem = new ItemStack(item, 1, meta);
                }
            }
            else continue;

            JEIModule.JEI_CATEGORY_MODIFICATION.put(key, value);
        }
    }
}
