package com.tttsaurus.ometweaks.mixins.jei;

import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.api.jei.DynamicCategoryWrapper;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.ingredients.IngredientRegistry;
import mezz.jei.recipes.RecipeRegistry;
import mezz.jei.startup.ModRegistry;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.*;
import java.util.stream.Stream;

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

        if (OMEConfig.ENABLE_JEI_CATEGORY_ORDER)
        {
            newRecipeCategories.clear();
            for (String uid: OMEConfig.JEI_CATEGORY_ORDER)
            {
                Stream<IRecipeCategory> stream = recipeCategories.stream().filter(category -> category.getUid().equals(uid));
                Optional<IRecipeCategory> first = stream.findFirst();
                if (first.isPresent())
                {
                    IRecipeCategory category = first.get();
                    newRecipeCategories.add(category);
                    recipeCategories.remove(category);
                }
            }
            newRecipeCategories.addAll(recipeCategories);
        }

        if (OMEConfig.ENABLE_JEI_CATEGORY_MODIFICATION)
        {
            Map<Integer, DynamicCategoryWrapper> substitutions = new HashMap<>();
            for (int i = 0; i < newRecipeCategories.size(); i++)
            {
                IRecipeCategory category = newRecipeCategories.get(i);
                for (Map.Entry<String, ResourceLocation> entry: OMEConfig.JEI_CATEGORY_MODIFICATION.entrySet())
                    if (category.getUid().equals(entry.getKey()))
                        substitutions.put(i, new DynamicCategoryWrapper(category, entry.getValue()));
            }
            for (Map.Entry<Integer, DynamicCategoryWrapper> entry: substitutions.entrySet())
                newRecipeCategories.set(entry.getKey(), entry.getValue());
        }

        recipeCategories.clear();
        recipeCategories.addAll(newRecipeCategories);
    }
}
