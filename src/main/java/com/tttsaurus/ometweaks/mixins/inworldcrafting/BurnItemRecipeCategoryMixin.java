package com.tttsaurus.ometweaks.mixins.inworldcrafting;

import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xt9.inworldcrafting.integrations.jei.BurnItemRecipeCategory;

@Mixin(BurnItemRecipeCategory.class)
public class BurnItemRecipeCategoryMixin
{
    /**
     * @author tttsaurus
     * @reason To support i18n, and this method is modified from xt9.inworldcrafting.integrations.jei.BurnItemRecipeCategory.getTitle
     */
    @Overwrite(remap = false)
    public String getTitle()
    {
        if (OMEConfig.ENABLE_IWC_JEI_I18N)
            return I18n.format("ometweaks.inworldcrafting.jei.burn_item_recipe.str1");
        else
            return "Burning Items";
    }
}
