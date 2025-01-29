package com.tttsaurus.ometweaks.api.industrialforegoing;

import com.buuz135.industrial.utils.Reference;
import com.tttsaurus.ometweaks.OMEConfig;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("all")
public class PetrifiedBurnTimeCategory implements IRecipeCategory<PetrifiedBurnTimeWrapper>
{
    private IGuiHelper guiHelper;

    public PetrifiedBurnTimeCategory(IGuiHelper guiHelper)
    {
        this.guiHelper = guiHelper;
    }

    @Override
    public String getUid()
    {
        return "petrified_burn_time";
    }

    @Override
    public String getTitle()
    {
        if (OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_JEI_OVERHAUL)
            return I18n.format("ometweaks.industrialforegoing.jei.petrified_fuel_gen.title");
        else
            return "Petrified Generator Burn Time";
    }

    @Override
    public String getModName()
    {
        return Reference.NAME;
    }

    @Override
    public IDrawable getBackground()
    {
        if (OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_JEI_OVERHAUL)
            return guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/container/furnace.png"), 55, 38 + 14, 18, 28, 3, 3, 0, 120);
        else
            return guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/container/furnace.png"), 55, 38 + 14, 18, 18, 3, 3, 0, 120);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PetrifiedBurnTimeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 3);
        guiItemStacks.set(ingredients);
    }
}
