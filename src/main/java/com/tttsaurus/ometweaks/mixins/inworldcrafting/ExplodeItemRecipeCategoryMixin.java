package com.tttsaurus.ometweaks.mixins.inworldcrafting;

import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import xt9.inworldcrafting.integrations.jei.ExplodeItemRecipeCategory;

@Mixin(ExplodeItemRecipeCategory.class)
public class ExplodeItemRecipeCategoryMixin
{
    /**
     * @author tttsaurus
     * @reason To support i18n, and this method is modified from xt9.inworldcrafting.integrations.jei.ExplodeItemRecipeCategory.getTitle
     */
    @Overwrite(remap = false)
    public String getTitle()
    {
        if (OMEConfig.ENABLE_IWC_JEI_I18N)
            return I18n.format("ometweaks.inworldcrafting.jei.explode_item_recipe.str1");
        else
            return "Exploding Items";
    }
}
