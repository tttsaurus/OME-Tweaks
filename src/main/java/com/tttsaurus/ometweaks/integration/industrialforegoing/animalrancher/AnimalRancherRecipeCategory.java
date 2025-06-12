package com.tttsaurus.ometweaks.integration.industrialforegoing.animalrancher;

import com.buuz135.industrial.proxy.BlockRegistry;
import com.buuz135.industrial.utils.Reference;
import com.tttsaurus.ometweaks.Tags;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("all")
public class AnimalRancherRecipeCategory implements IRecipeCategory<AnimalRancherRecipeWrapper>
{
    private final IGuiHelper guiHelper;
    private final IDrawable background, icon;

    public AnimalRancherRecipeCategory(IGuiHelper guiHelper)
    {
        this.guiHelper = guiHelper;
        background = guiHelper
                .drawableBuilder(new ResourceLocation(Tags.MODID, "textures/gui/jei/animal_rancher_bg.png"), 0, 0, 140, 44)
                .setTextureSize(140, 44)
                .build();
        icon = guiHelper.createDrawableIngredient(new ItemStack(BlockRegistry.animalResourceHarvesterBlock));
    }

    @Override
    public String getUid()
    {
        return "animal_rancher";
    }

    @Override
    public String getTitle()
    {
        return I18n.format("ometweaks.industrialforegoing.jei.animal_rancher.title");
    }

    @Override
    public String getModName()
    {
        return Reference.NAME;
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AnimalRancherRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        if (recipeWrapper.output.itemStack != null)
            guiItemStacks.init(0, false, 108, 14);

        if (recipeWrapper.output.fluidStack != null)
            guiFluidStacks.init(0, false, 92, 15, 16, 16, recipeWrapper.output.fluidStack.amount, true, null);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }
}
