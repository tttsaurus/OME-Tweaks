package com.tttsaurus.ometweaks.integration.jei;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.gui.elements.DrawableBuilder;
import mezz.jei.gui.elements.DrawableIngredient;
import mezz.jei.plugins.vanilla.ingredients.item.ItemStackRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import java.util.List;

@SuppressWarnings("all")
public class DynamicCategoryWrapper<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
    private final IRecipeCategory<T> originalCategory;
    private IDrawable newIcon = null;

    public DynamicCategoryWrapper(IRecipeCategory<T> originalCategory, ResourceLocation iconRL)
    {
        this.originalCategory = originalCategory;
        newIcon = (new DrawableBuilder(iconRL, 0, 0, 16, 16)).setTextureSize(16, 16).build();
    }
    public DynamicCategoryWrapper(IRecipeCategory<T> originalCategory, ItemStack itemStack)
    {
        this.originalCategory = originalCategory;
        if (itemStack != null)
            newIcon = new DrawableIngredient<>(itemStack, new ItemStackRenderer());
    }

    private IDrawableGhostItemHandler drawableGhostItemHandler = null;
    public void setDrawableGhostItemHandler(CategoryModification categoryMod)
    {
        drawableGhostItemHandler = () ->
        {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(categoryMod.itemRegistryName));
            if (item == null) return;
            ItemStack itemStack = new ItemStack(item, 1, categoryMod.itemMeta);
            newIcon = new DrawableIngredient<>(itemStack, new ItemStackRenderer());
        };
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
        if (drawableGhostItemHandler != null)
        {
            drawableGhostItemHandler.handle();
            drawableGhostItemHandler = null;
        }
        return newIcon == null ? originalCategory.getIcon() : newIcon;
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
