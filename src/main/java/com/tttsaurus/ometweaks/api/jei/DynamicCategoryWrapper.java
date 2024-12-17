package com.tttsaurus.ometweaks.api.jei;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.elements.DrawableBuilder;
import mezz.jei.gui.elements.DrawableIngredient;
import mezz.jei.plugins.vanilla.ingredients.item.ItemStackRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import java.util.List;

@SuppressWarnings("all")
public class DynamicCategoryWrapper<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
    private final IRecipeCategory<T> originalCategory;
    private final IDrawable newIcon;

    public DynamicCategoryWrapper(IRecipeCategory<T> originalCategory, ResourceLocation iconRL)
    {
        this.originalCategory = originalCategory;
        newIcon = (new DrawableBuilder(iconRL, 0, 0, 16, 16)).setTextureSize(16, 16).build();
    }
    public DynamicCategoryWrapper(IRecipeCategory<T> originalCategory, ItemStack itemStack)
    {
        this.originalCategory = originalCategory;
        newIcon = new DrawableIngredient<>(itemStack, new ItemStackRenderer());
    }

    @Override
    public String getUid()
    {
        return originalCategory.getUid();
    }

    @Override
    public String getTitle()
    {
        return originalCategory.getTitle();
    }

    @Override
    public String getModName()
    {
        return originalCategory.getModName();
    }

    @Override
    public IDrawable getBackground()
    {
        return originalCategory.getBackground();
    }

    @Override
    public IDrawable getIcon()
    {
        return newIcon != null ? newIcon : originalCategory.getIcon();
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
        originalCategory.drawExtras(minecraft);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, T recipeWrapper, IIngredients ingredients)
    {
        originalCategory.setRecipe(recipeLayout, recipeWrapper, ingredients);
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        return originalCategory.getTooltipStrings(mouseX, mouseY);
    }
}
