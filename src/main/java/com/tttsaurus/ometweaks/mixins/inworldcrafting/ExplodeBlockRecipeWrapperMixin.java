package com.tttsaurus.ometweaks.mixins.inworldcrafting;

import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xt9.inworldcrafting.common.recipe.ExplodeBlockRecipe;
import xt9.inworldcrafting.integrations.jei.ExplodeBlockRecipeWrapper;

@SuppressWarnings("all")
@Mixin(ExplodeBlockRecipeWrapper.class)
public class ExplodeBlockRecipeWrapperMixin
{
    @Shadow(remap = false)
    private ExplodeBlockRecipe recipe;

    /**
     * @author tttsaurus
     * @reason To support i18n, and this method is modified from xt9.inworldcrafting.integrations.jei.ExplodeBlockRecipeWrapper.drawInfo
     */
    @Overwrite(remap = false)
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        if (OMEConfig.ENABLE_IWC_JEI_I18N)
        {
            FontRenderer renderer = minecraft.fontRenderer;
            renderer.drawStringWithShadow(I18n.format("ometweaks.inworldcrafting.jei.explode_block_recipe.str2", recipe.getItemSpawnChance() + "%"), 1, 30, 0xFFFFFF);
        }
        else
        {
            FontRenderer renderer = minecraft.fontRenderer;
            renderer.drawStringWithShadow("Success chance: " + recipe.getItemSpawnChance() + "%", 1, 30, 0xFFFFFF);
        }
    }
}
