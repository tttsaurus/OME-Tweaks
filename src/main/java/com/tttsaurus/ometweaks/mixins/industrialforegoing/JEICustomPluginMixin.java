package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.jei.JEICustomPlugin;
import com.buuz135.industrial.proxy.BlockRegistry;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.*;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.registry.EntityEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.*;
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
        if (IndustrialForegoingModule.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            List<PetrifiedBurnTimeWrapper> petrifiedBurnTimeWrappers = new ArrayList<>();
            List<ItemStack> modifiedItems = new CopyOnWriteArrayList<>(IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_FUELS.keySet());

            float burnTimeMultiplier = BlockRegistry.petrifiedFuelGeneratorBlock.getBurnTimeMultiplier();

            instance.getIngredientRegistry().getFuels().stream().filter(PetrifiedFuelGeneratorTile::acceptsInputStack).forEach(itemStack ->
            {
                boolean modified = false;
                for (ItemStack modifiedItem: modifiedItems)
                {
                    if (itemStack.isItemEqual(modifiedItem))
                    {
                        modifiedItems.remove(modifiedItem);
                        FuelDef fuelDef = IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_FUELS.get(modifiedItem);
                        petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(modifiedItem, (int)(fuelDef.duration * burnTimeMultiplier), fuelDef.rate));
                        modified = true;
                        break;
                    }
                }
                if (!modified)
                {
                    int duration = TileEntityFurnace.getItemBurnTime(itemStack);
                    int rate = (int)PetrifiedFuelGeneratorTile.getEnergy(duration);
                    if (IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX != -1)
                        duration = Math.min(duration, IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX);
                    if (IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX != -1)
                        rate = Math.min(rate, IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX);
                    petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(itemStack, (int)(duration * burnTimeMultiplier), rate));
                }
            });

            for (ItemStack modifiedItem: modifiedItems)
            {
                FuelDef fuelDef = IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_FUELS.get(modifiedItem);
                petrifiedBurnTimeWrappers.add(new PetrifiedBurnTimeWrapper(modifiedItem, (int)(fuelDef.duration * burnTimeMultiplier), fuelDef.rate));
            }

            if (!IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_JEI_ORDER.equals("NONE"))
            {
                if (IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_JEI_ORDER.equals("BIGGER_FIRST"))
                    Collections.sort(petrifiedBurnTimeWrappers, Comparator.comparing(PetrifiedBurnTimeWrapper::getRate).reversed());
                else if (IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_JEI_ORDER.equals("SMALLER_FIRST"))
                    Collections.sort(petrifiedBurnTimeWrappers, Comparator.comparing(PetrifiedBurnTimeWrapper::getRate));
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
        if (IndustrialForegoingModule.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            OME_Tweaks$petrifiedBurnTimeCategory = new PetrifiedBurnTimeCategory(instance.getJeiHelpers().getGuiHelper());
            instance.addRecipeCategories(new IRecipeCategory[]{OME_Tweaks$petrifiedBurnTimeCategory});
        }
        else
            original.call(instance, iRecipeCategories);
    }

    @Unique
    private AnimalRancherRecipeCategory OME_Tweaks$animalRancherCategory;

    @Inject(method = "register", at = @At("HEAD"), remap = false)
    public void addAnimalRancherRecipes(IModRegistry registry, CallbackInfo ci)
    {
        if (IndustrialForegoingModule.ENABLE_IF_CUSTOM_ANIMAL_RANCHER &&
                IndustrialForegoingModule.ENABLE_IF_CUSTOM_ANIMAL_RANCHER_JEI &&
                BlockRegistry.animalResourceHarvesterBlock.isEnabled())
        {
            List<AnimalRancherRecipeWrapper> animalRancherRecipeWrappers = new ArrayList<>();
            for (Map.Entry<EntityEntry, AnimalRancherOutput> entry: IndustrialForegoingModule.IF_CUSTOM_ANIMAL_RANCHER_RECIPES.entrySet())
                animalRancherRecipeWrappers.add(new AnimalRancherRecipeWrapper(entry.getKey(), entry.getValue()));
            registry.addRecipes(animalRancherRecipeWrappers, OME_Tweaks$animalRancherCategory.getUid());
            registry.addRecipeCatalyst(new ItemStack(BlockRegistry.animalResourceHarvesterBlock), OME_Tweaks$animalRancherCategory.getUid());
        }
    }

    @Inject(method = "registerCategories", at = @At("HEAD"), remap = false)
    public void addAnimalRancherCategory(IRecipeCategoryRegistration registry, CallbackInfo ci)
    {
        if (IndustrialForegoingModule.ENABLE_IF_CUSTOM_ANIMAL_RANCHER &&
                IndustrialForegoingModule.ENABLE_IF_CUSTOM_ANIMAL_RANCHER_JEI &&
                BlockRegistry.animalResourceHarvesterBlock.isEnabled())
        {
            OME_Tweaks$animalRancherCategory = new AnimalRancherRecipeCategory(registry.getJeiHelpers().getGuiHelper());
            registry.addRecipeCategories(new IRecipeCategory[]{OME_Tweaks$animalRancherCategory});
        }
    }
}
