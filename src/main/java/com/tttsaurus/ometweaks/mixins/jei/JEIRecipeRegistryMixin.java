package com.tttsaurus.ometweaks.mixins.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.tttsaurus.ometweaks.OMEConfig;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeRegistryPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.collect.ListMultiMap;
import mezz.jei.gui.recipes.RecipeClickableArea;
import mezz.jei.ingredients.IngredientRegistry;
import mezz.jei.recipes.RecipeRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("all")
@Mixin(RecipeRegistry.class)
public class JEIRecipeRegistryMixin
{
    @Mutable
    @Final
    @Shadow(remap = false)
    private ImmutableList<IRecipeCategory> recipeCategories;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    public void onConstruct(
            List<IRecipeCategory> recipeCategories,
            List<IRecipeHandler> unsortedRecipeHandlers,
            ListMultiMap<String, IRecipeHandler> recipeHandlers,
            ImmutableTable<Class, String, IRecipeTransferHandler> recipeTransferHandlers,
            List<Object> unsortedRecipes,
            ListMultiMap<String, Object> recipes,
            ListMultiMap<Class<? extends GuiContainer>, RecipeClickableArea> recipeClickableAreasMap,
            ListMultiMap<String, Object> recipeCatalysts,
            IngredientRegistry ingredientRegistry,
            List<IRecipeRegistryPlugin> plugins,
            CallbackInfo ci
    )
    {
        if (!(OMEConfig.ENABLE && OMEConfig.ENABLE_JEI_CATEGORY_ORDER)) return;

        List<IRecipeCategory> list = new ArrayList<>();
        for (String uid: OMEConfig.JEI_CATEGORY_ORDER)
        {
            Stream<IRecipeCategory> stream = recipeCategories.stream().filter(category -> category.getUid().equals(uid));
            Optional<IRecipeCategory> first = stream.findFirst();
            if (first.isPresent())
            {
                IRecipeCategory category = first.get();
                list.add(category);
                recipeCategories.remove(category);
            }
        }
        list.addAll(recipeCategories);

        this.recipeCategories = ImmutableList.copyOf(list);
    }
}
