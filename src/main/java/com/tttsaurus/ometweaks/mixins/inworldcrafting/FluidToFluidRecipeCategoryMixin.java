package com.tttsaurus.ometweaks.mixins.inworldcrafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.inworldcrafting.InWorldCraftingModule;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import xt9.inworldcrafting.integrations.jei.FluidToFluidRecipeCategory;

@SuppressWarnings("all")
@Mixin(FluidToFluidRecipeCategory.class)
public class FluidToFluidRecipeCategoryMixin
{
    /**
     * @author tttsaurus
     * @reason To support i18n, and this method is modified from xt9.inworldcrafting.integrations.jei.FluidToFluidRecipeCategory.getTitle
     */
    @WrapMethod(method = "getTitle", remap = false)
    public String getTitle(Operation<String> original)
    {
        if (InWorldCraftingModule.ENABLE_IWC_JEI_I18N)
            return I18n.format("ometweaks.inworldcrafting.jei.fluid2fluid_recipe.str1");
        else
            return original.call();
    }
}
