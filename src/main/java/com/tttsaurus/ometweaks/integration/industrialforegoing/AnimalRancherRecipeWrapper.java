package com.tttsaurus.ometweaks.integration.industrialforegoing;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.entity.Entity;

public class AnimalRancherRecipeWrapper implements IRecipeWrapper
{
    public final Class<? extends Entity> inputEntity;
    public final AnimalRancherOutput output;

    public AnimalRancherRecipeWrapper(Class<? extends Entity> inputEntity, AnimalRancherOutput output)
    {
        this.inputEntity = inputEntity;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        if (output.itemStack != null)
            ingredients.setOutput(VanillaTypes.ITEM, output.itemStack);
        if (output.fluidStack != null)
            ingredients.setOutput(VanillaTypes.FLUID, output.fluidStack);
    }
}
