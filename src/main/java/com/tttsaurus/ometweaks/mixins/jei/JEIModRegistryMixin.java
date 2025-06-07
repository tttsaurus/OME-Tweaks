package com.tttsaurus.ometweaks.mixins.jei;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.integration.jei.CategoryModification;
import com.tttsaurus.ometweaks.integration.jei.DynamicCategoryWrapper;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.ingredients.IngredientRegistry;
import mezz.jei.recipes.RecipeRegistry;
import mezz.jei.startup.ModRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.*;

@SuppressWarnings("all")
@Mixin(ModRegistry.class)
public class JEIModRegistryMixin
{
    @Final
    @Shadow(remap = false)
    private List<IRecipeCategory> recipeCategories;

    @Inject(method = "createRecipeRegistry", at = @At("HEAD"), remap = false)
    public void createRecipeRegistry(IngredientRegistry ingredientRegistry, CallbackInfoReturnable<RecipeRegistry> cir)
    {
        List<IRecipeCategory> newRecipeCategories = new ArrayList<>();
        newRecipeCategories.addAll(recipeCategories);

        if (OMEConfig.ENABLE_JEI_CATEGORY_MODIFICATION)
        {
            Map<Integer, DynamicCategoryWrapper> substitutions = new HashMap<>();
            for (int i = 0; i < newRecipeCategories.size(); i++)
            {
                IRecipeCategory category = newRecipeCategories.get(i);
                for (Map.Entry<String, CategoryModification> entry: OMEConfig.JEI_CATEGORY_MODIFICATION.entrySet())
                    if (category.getUid().equals(entry.getKey()))
                    {
                        CategoryModification categoryMod = entry.getValue();
                        if (categoryMod.iconRL != null)
                            substitutions.put(i, new DynamicCategoryWrapper(category, categoryMod.iconRL));
                        else if (categoryMod.iconItem != null || categoryMod.isGhostItem)
                        {
                            DynamicCategoryWrapper dynamicCategoryWrapper = new DynamicCategoryWrapper(category, categoryMod.iconItem);
                            if (categoryMod.isGhostItem)
                                dynamicCategoryWrapper.setDrawableGhostItemHandler(categoryMod);
                            substitutions.put(i, dynamicCategoryWrapper);
                        }
                    }
            }
            for (Map.Entry<Integer, DynamicCategoryWrapper> entry: substitutions.entrySet())
                newRecipeCategories.set(entry.getKey(), entry.getValue());
        }

        recipeCategories.clear();
        recipeCategories.addAll(newRecipeCategories);
    }
}
