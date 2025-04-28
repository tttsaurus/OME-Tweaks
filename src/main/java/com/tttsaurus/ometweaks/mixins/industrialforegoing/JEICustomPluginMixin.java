package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.jei.JEICustomPlugin;
import com.buuz135.industrial.proxy.BlockRegistry;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.misc.industrialforegoing.FuelDef;
import com.tttsaurus.ometweaks.misc.industrialforegoing.PetrifiedBurnTimeCategory;
import com.tttsaurus.ometweaks.misc.industrialforegoing.PetrifiedBurnTimeWrapper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("all")
@Mixin(JEICustomPlugin.class)
public class JEICustomPluginMixin
{
    @Unique
    private PetrifiedBurnTimeCategory OME_Tweaks$petrifiedBurnTimeCategory;

    @WrapOperation(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/IModRegistry;addRecipes(Ljava/util/Collection;Ljava/lang/String;)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lmezz/jei/api/IModRegistry;getIngredientRegistry()Lmezz/jei/api/ingredients/IIngredientRegistry;"),
                    to = @At(value = "INVOKE", target = "Lcom/buuz135/industrial/book/BookCategory;getEntries()Ljava/util/Map;")
            ),
            remap = false)
    public void addMyPetrifiedFuelRecipes(IModRegistry instance, Collection<?> objects, String s, Operation<Void> original)
    {
        if (OMEConfig.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            List<PetrifiedBurnTimeWrapper> petrifiedBurnTimeWrappers = new ArrayList<>();
            List<ItemStack> modifiedItems = new CopyOnWriteArrayList<>(OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.keySet());

            float burnTimeMultiplier = BlockRegistry.petrifiedFuelGeneratorBlock.getBurnTimeMultiplier();

            instance.getIngredientRegistry().getFuels().stream().filter(PetrifiedFuelGeneratorTile::acceptsInputStack).forEach(itemStack ->
            {
                boolean modified = false;
                for (ItemStack modifiedItem: modifiedItems)
                {
                    if (itemStack.isItemEqual(modifiedItem))
                    {
                        modifiedItems.remove(modifiedItem);
                        FuelDef fuelDef = OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.get(modifiedItem);
                        petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(modifiedItem, (int)(fuelDef.duration * burnTimeMultiplier), fuelDef.rate));
                        modified = true;
                        break;
                    }
                }
                if (!modified)
                {
                    int duration = TileEntityFurnace.getItemBurnTime(itemStack);
                    int rate = (int)PetrifiedFuelGeneratorTile.getEnergy(duration);
                    if (OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX != -1)
                        duration = duration > OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX ?
                                OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX : duration;
                    if (OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX != -1)
                        rate = rate > OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX ?
                                OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX : rate;
                    petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(itemStack, (int)(duration * burnTimeMultiplier), rate));
                }
            });

            for (ItemStack modifiedItem: modifiedItems)
            {
                FuelDef fuelDef = OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.get(modifiedItem);
                petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(modifiedItem, (int)(fuelDef.duration * burnTimeMultiplier), fuelDef.rate));
            }

            instance.addRecipes(petrifiedBurnTimeWrappers, OME_Tweaks$petrifiedBurnTimeCategory.getUid());
        }
        else
            original.call(instance, objects, s);
    }

    @WrapOperation(
            method = "registerCategories",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/recipe/IRecipeCategoryRegistration;addRecipeCategories([Lmezz/jei/api/recipe/IRecipeCategory;)V"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lcom/buuz135/industrial/jei/petrifiedgen/PetrifiedBurnTimeCategory;<init>(Lmezz/jei/api/IGuiHelper;)V"),
                    to = @At(value = "INVOKE", target = "Lcom/buuz135/industrial/jei/fluiddictionary/FluidDictionaryCategory;<init>(Lmezz/jei/api/IGuiHelper;)V")
            ),
            remap = false)
    public void addMyPetrifiedBurnTimeCategory(IRecipeCategoryRegistration instance, IRecipeCategory[] iRecipeCategories, Operation<Void> original)
    {
        if (OMEConfig.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            OME_Tweaks$petrifiedBurnTimeCategory = new PetrifiedBurnTimeCategory(instance.getJeiHelpers().getGuiHelper());

            instance.addRecipeCategories(new IRecipeCategory[]{OME_Tweaks$petrifiedBurnTimeCategory});
        }
        else
            original.call(instance, iRecipeCategories);
    }
}
