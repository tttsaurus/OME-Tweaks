package com.tttsaurus.ometweaks.mixins.inworldcrafting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.inworldcrafting.InWorldCraftingModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xt9.inworldcrafting.common.recipe.ExplodeItemRecipe;
import xt9.inworldcrafting.integrations.jei.ExplodeItemRecipeWrapper;

@SuppressWarnings("all")
@Mixin(ExplodeItemRecipeWrapper.class)
public class ExplodeItemRecipeWrapperMixin
{
    @Shadow(remap = false)
    private ExplodeItemRecipe recipe;

    /**
     * @author tttsaurus
     * @reason To support i18n, and this method is modified from xt9.inworldcrafting.integrations.jei.ExplodeItemRecipeWrapper.drawInfo
     */
    @WrapMethod(method = "drawInfo", remap = false)
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY, Operation<Void> original)
    {
        if (InWorldCraftingModule.ENABLE_IWC_JEI_I18N)
        {
            FontRenderer renderer = minecraft.fontRenderer;
            renderer.drawStringWithShadow(I18n.format("ometweaks.inworldcrafting.jei.explode_item_recipe.str2", recipe.getSurviveChance() + "%"), 1, 30, 0xFFFFFF);
        }
        else
            original.call(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
    }
}
