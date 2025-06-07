package com.tttsaurus.ometweaks.mixins.inworldcrafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.inworldcrafting.InWorldCraftingModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xt9.inworldcrafting.common.recipe.FluidToItemRecipe;
import xt9.inworldcrafting.integrations.jei.FluidToItemRecipeWrapper;

@SuppressWarnings("all")
@Mixin(FluidToItemRecipeWrapper.class)
public class FluidToItemRecipeWrapperMixin
{
    @Shadow(remap = false)
    private FluidToItemRecipe recipe;

    /**
     * @author tttsaurus
     * @reason To support i18n, and this method is modified from xt9.inworldcrafting.integrations.jei.FluidToItemRecipeWrapper.drawInfo
     */
    @WrapMethod(method = "drawInfo", remap = false)
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY, Operation<Void> original)
    {
        if (InWorldCraftingModule.ENABLE_IWC_JEI_I18N)
        {
            FontRenderer renderer = minecraft.fontRenderer;
            renderer.drawStringWithShadow(I18n.format("ometweaks.inworldcrafting.jei.fluid2item_recipe.str2"), 2, 26, 0xFFFFFF);

            if(recipe.willConsume())
                renderer.drawStringWithShadow(I18n.format("ometweaks.inworldcrafting.jei.yes"), 84, 26, 0xcc0036);
            else
                renderer.drawStringWithShadow(I18n.format("ometweaks.inworldcrafting.jei.no"), 84, 26, 0xc9f26a);
        }
        else
            original.call(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
    }
}
