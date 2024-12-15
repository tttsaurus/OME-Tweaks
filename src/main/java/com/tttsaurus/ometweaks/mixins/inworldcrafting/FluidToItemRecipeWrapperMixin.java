package com.tttsaurus.ometweaks.mixins.inworldcrafting;

import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
    @Overwrite(remap = false)
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        if (OMEConfig.ENABLE_IWC_JEI_I18N)
        {
            FontRenderer renderer = minecraft.fontRenderer;
            renderer.drawStringWithShadow(I18n.format("ometweaks.inworldcrafting.jei.fluid2item_recipe.str2"), 2, 26, 0xFFFFFF);

            if(recipe.willConsume())
                renderer.drawStringWithShadow(I18n.format("ometweaks.inworldcrafting.jei.yes"), 84, 26, 0xcc0036);
            else
                renderer.drawStringWithShadow(I18n.format("ometweaks.inworldcrafting.jei.no"), 84, 26, 0xc9f26a);
        }
        else
        {
            FontRenderer renderer = minecraft.fontRenderer;
            renderer.drawStringWithShadow("Consumes Fluid: ", 2, 26, 0xFFFFFF);

            if(recipe.willConsume())
                renderer.drawStringWithShadow("Yes", 84, 26, 0xcc0036);
            else
                renderer.drawStringWithShadow("No", 84, 26, 0xc9f26a);
        }
    }
}
