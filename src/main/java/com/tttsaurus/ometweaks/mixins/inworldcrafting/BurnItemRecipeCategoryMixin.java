package com.tttsaurus.ometweaks.mixins.inworldcrafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import xt9.inworldcrafting.integrations.jei.BurnItemRecipeCategory;

@SuppressWarnings("all")
@Mixin(BurnItemRecipeCategory.class)
public class BurnItemRecipeCategoryMixin
{
    /**
     * @author tttsaurus
     * @reason To support i18n, and this method is modified from xt9.inworldcrafting.integrations.jei.BurnItemRecipeCategory.getTitle
     */
    @WrapMethod(method = "getTitle", remap = false)
    public String getTitle(Operation<String> original)
    {
        if (OMEConfig.ENABLE_IWC_JEI_I18N)
            return I18n.format("ometweaks.inworldcrafting.jei.burn_item_recipe.str1");
        else
            return original.call();
    }
}
