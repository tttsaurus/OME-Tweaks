package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.generator.AbstractFuelGenerator;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.generator.petrified.FuelDef;
import com.tttsaurus.ometweaks.integration.industrialforegoing.generator.petrified.IFuelGetter;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Map;

@Mixin(PetrifiedFuelGeneratorTile.class)
public class PetrifiedFuelGeneratorTileMixin
{
    @Unique
    public IFuelGetter OME_Tweaks$fuelGetter;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    public void onConstruct(CallbackInfo ci)
    {
        AbstractFuelGenerator this0 = (AbstractFuelGenerator)(Object)this;
        try
        {
            Class<?> clazz = AbstractFuelGenerator.class;
            Field field = clazz.getDeclaredField("current");
            field.setAccessible(true);
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle methodHandle = lookup.unreflectGetter(field);
            OME_Tweaks$fuelGetter = () ->
            {
                try
                {
                    return (ItemStack)methodHandle.invoke(this0);
                }
                catch (Throwable ignored) { return null; }
            };
        }
        catch (Exception ignored) { }
    }

    /**
     * @author tttsaurus
     * @reason To support custom fuels, and this method is modified from com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile.getEnergyProduced
     */
    @WrapMethod(method = "getEnergyProduced", remap = false)
    public long getEnergyProduced(int burnTime, Operation<Long> original)
    {
        if (IndustrialForegoingModule.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            ItemStack itemStack = OME_Tweaks$fuelGetter.get();
            if (itemStack == null) return PetrifiedFuelGeneratorTile.getEnergy(burnTime);

            burnTime = TileEntityFurnace.getItemBurnTime(itemStack);
            long power = PetrifiedFuelGeneratorTile.getEnergy(burnTime);
            if (IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX != -1)
                power = power > IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX ?
                        IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_POWER_MAX : power;

            for (Map.Entry<ItemStack, FuelDef> entry: IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_FUELS.entrySet())
                if (itemStack.isItemEqual(entry.getKey()))
                    return entry.getValue().rate;
            return power;
        }
        else
            return original.call(burnTime);
    }

    /**
     * @author tttsaurus
     * @reason To support custom fuels, and this method is modified from com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile.acceptsInputStack
     */
    @WrapMethod(method = "acceptsInputStack", remap = false)
    private static boolean acceptsInputStack(ItemStack stack, Operation<Boolean> original)
    {
        if (IndustrialForegoingModule.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            boolean accept = original.call(stack);

            for (ItemStack fuel: IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_FUELS.keySet())
                if (stack.isItemEqual(fuel) && !stack.isEmpty())
                    accept = true;

            return accept;
        }
        else
            return original.call(stack);
    }
}
