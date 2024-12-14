package com.tttsaurus.ometweaks.mixins.jei;

import com.google.common.collect.ImmutableList;
import com.tttsaurus.ometweaks.OMEConfig;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.recipes.RecipeCategoryComparator;
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
@Mixin(RecipeCategoryComparator.class)
public class JEIRecipeCategoryComparatorMixin
{
    @Mutable
    @Final
    @Shadow(remap = false)
    private ImmutableList<String> recipeCategories;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    public void onConstruct(List<IRecipeCategory> recipeCategories, CallbackInfo ci)
    {
        if (!OMEConfig.ENABLE_JEI_CATEGORY_ORDER) return;

        List<String> list = new ArrayList<>();
        for (String uid: OMEConfig.JEI_CATEGORY_ORDER)
        {
            Stream<IRecipeCategory> stream = recipeCategories.stream().filter(category -> category.getUid().equals(uid));
            Optional<IRecipeCategory> first = stream.findFirst();
            if (first.isPresent()) list.add(uid);
        }
        for (IRecipeCategory category: recipeCategories)
        {
            String uid = category.getUid();
            if (!list.contains(uid)) list.add(uid);
        }

        this.recipeCategories = ImmutableList.copyOf(list);
    }
}
