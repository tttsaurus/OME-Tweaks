package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.agriculture.AnimalResourceHarvesterTile;
import com.buuz135.industrial.utils.ItemStackUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.AnimalRancherOutput;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;

@Mixin(AnimalResourceHarvesterTile.class)
public class AnimalResourceHarvesterTileMixin
{
    @Shadow(remap = false)
    private ItemStackHandler outItems;
    @Shadow(remap = false)
    private IFluidTank tank;

    @Unique
    private boolean OME_Tweaks$hasWorked = false;

    @Inject(method = "work", at = @At("TAIL"), cancellable = true, remap = false)
    public void afterWork(CallbackInfoReturnable<Float> cir)
    {
        if (!IndustrialForegoingModule.ENABLE_IF_CUSTOM_ANIMAL_RANCHER) return;
        if (OME_Tweaks$hasWorked)
        {
            OME_Tweaks$hasWorked = false;
            cir.setReturnValue(1f);
        }
    }

    @WrapOperation(
            method = "work",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/buuz135/industrial/tile/agriculture/AnimalResourceHarvesterTile;getFortuneLevel()I"
            ),
            remap = false)
    public int extraWork(AnimalResourceHarvesterTile instance, Operation<Integer> original)
    {
        if (!IndustrialForegoingModule.ENABLE_IF_CUSTOM_ANIMAL_RANCHER)
            return original.call(instance);

        OME_Tweaks$hasWorked = false;
        int fortune = instance.getFortuneLevel();

        for (Map.Entry<Class<? extends Entity>, AnimalRancherOutput> entry: IndustrialForegoingModule.IF_CUSTOM_ANIMAL_RANCHER_RECIPES.entrySet())
        {
            AnimalRancherOutput output = entry.getValue();
            for (Entity entity: instance.getWorld().getEntitiesWithinAABB(entry.getKey(), instance.getWorkingArea()))
            {
                if (instance.getWorld().rand.nextFloat() < output.chance)
                {
                    if (output.itemStack != null && !ItemStackUtils.isInventoryFull(outItems))
                    {
                        if (!IndustrialForegoingModule.ENABLE_IF_CUSTOM_ANIMAL_RANCHER_FORTUNE || fortune <= 0)
                            ItemHandlerHelper.insertItem(outItems, output.itemStack.copy(), false);
                        else
                        {
                            for (int i = 0; i < fortune + 1; i++)
                                ItemHandlerHelper.insertItem(outItems, output.itemStack.copy(), false);
                        }

                        OME_Tweaks$hasWorked = true;
                    }
                    if (output.fluidStack != null)
                    {
                        if (!IndustrialForegoingModule.ENABLE_IF_CUSTOM_ANIMAL_RANCHER_FORTUNE || fortune <= 0)
                            tank.fill(output.fluidStack.copy(), true);
                        else
                        {
                            for (int i = 0; i < fortune + 1; i++)
                                tank.fill(output.fluidStack.copy(), true);
                        }

                        OME_Tweaks$hasWorked = true;
                    }
                    if (output.damage != 0 && OME_Tweaks$hasWorked)
                        entity.attackEntityFrom(DamageSource.GENERIC, output.damage);
                }
            }
        }

        return original.call(instance);
    }
}
