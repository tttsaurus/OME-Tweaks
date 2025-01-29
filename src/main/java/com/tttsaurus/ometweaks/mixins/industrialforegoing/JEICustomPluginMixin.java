package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.jei.JEICustomPlugin;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.OMETweaks;
import com.tttsaurus.ometweaks.api.industrialforegoing.FuelDef;
import com.tttsaurus.ometweaks.api.industrialforegoing.PetrifiedBurnTimeCategory;
import com.tttsaurus.ometweaks.api.industrialforegoing.PetrifiedBurnTimeWrapper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(JEICustomPlugin.class)
public class JEICustomPluginMixin
{
    @Unique
    private PetrifiedBurnTimeCategory OME_Tweaks$petrifiedBurnTimeCategory;

    @Inject(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/IModRegistry;addRecipes(Ljava/util/Collection;Ljava/lang/String;)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lmezz/jei/api/IModRegistry;getIngredientRegistry()Lmezz/jei/api/ingredients/IIngredientRegistry;"),
                    to = @At(value = "INVOKE", target = "Lcom/buuz135/industrial/book/BookCategory;getEntries()Ljava/util/Map;")
            ),
            cancellable = true,
            remap = false)
    public void injectMyPetrifiedFuelRecipes(IModRegistry registry, CallbackInfo ci)
    {
        OMETweaks.LOGGER.info("triggered");

        if (OMEConfig.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            ci.cancel();
            List<PetrifiedBurnTimeWrapper> petrifiedBurnTimeWrappers = new ArrayList<>();
            List<ItemStack> modifiedItems = new CopyOnWriteArrayList<>(OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.keySet());

            registry.getIngredientRegistry().getFuels().stream().filter(PetrifiedFuelGeneratorTile::acceptsInputStack).forEach(itemStack ->
            {
                boolean modified = false;
                for (ItemStack modifiedItem: modifiedItems)
                {
                    if (itemStack.isItemEqual(modifiedItem))
                    {
                        modifiedItems.remove(modifiedItem);
                        FuelDef fuelDef = OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.get(modifiedItem);
                        petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(modifiedItem, fuelDef.duration, fuelDef.rate));
                        modified = true;
                        break;
                    }
                }
                if (!modified)
                    petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(itemStack, TileEntityFurnace.getItemBurnTime(itemStack)));
            });

            for (ItemStack modifiedItem: modifiedItems)
            {
                FuelDef fuelDef = OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.get(modifiedItem);
                petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(modifiedItem, fuelDef.duration, fuelDef.rate));
            }

            registry.addRecipes(petrifiedBurnTimeWrappers, OME_Tweaks$petrifiedBurnTimeCategory.getUid());
        }
    }

    @Inject(
            method = "registerCategories",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/recipe/IRecipeCategoryRegistration;addRecipeCategories([Lmezz/jei/api/recipe/IRecipeCategory;)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lcom/buuz135/industrial/jei/petrifiedgen/PetrifiedBurnTimeCategory;<init>(Lmezz/jei/api/IGuiHelper;)V"),
                    to = @At(value = "INVOKE", target = "Lcom/buuz135/industrial/jei/fluiddictionary/FluidDictionaryCategory;<init>(Lmezz/jei/api/IGuiHelper;)V")
            ),
            cancellable = true,
            remap = false)
    public void injectMyPetrifiedBurnTimeCategory(IRecipeCategoryRegistration registry, CallbackInfo ci)
    {
        if (OMEConfig.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            ci.cancel();
            OME_Tweaks$petrifiedBurnTimeCategory = new PetrifiedBurnTimeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(new IRecipeCategory[]{OME_Tweaks$petrifiedBurnTimeCategory});
        }
    }
}
