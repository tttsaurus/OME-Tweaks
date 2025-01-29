package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.generator.AbstractFuelGenerator;
import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.api.industrialforegoing.FuelDef;
import com.tttsaurus.ometweaks.api.industrialforegoing.IFuelGetter;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
    @Overwrite(remap = false)
    public long getEnergyProduced(int burnTime)
    {
        if (OMEConfig.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            ItemStack itemStack = OME_Tweaks$fuelGetter.get();
            if (itemStack == null) return PetrifiedFuelGeneratorTile.getEnergy(burnTime);

            for (Map.Entry<ItemStack, FuelDef> entry: OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.entrySet())
                if (itemStack.isItemEqual(entry.getKey()))
                    return entry.getValue().rate;
            return PetrifiedFuelGeneratorTile.getEnergy(burnTime);
        }
        else
            return PetrifiedFuelGeneratorTile.getEnergy(burnTime);
    }

    /**
     * @author tttsaurus
     * @reason To support custom fuels, and this method is modified from com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile.acceptsInputStack
     */
    @Overwrite(remap = false)
    public static boolean acceptsInputStack(ItemStack stack)
    {
        if (OMEConfig.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
        {
            boolean accept = !stack.isEmpty() && TileEntityFurnace.isItemFuel(stack) && !stack.getItem().equals(Items.LAVA_BUCKET) && !stack.getItem().equals(ForgeModContainer.getInstance().universalBucket) && PetrifiedFuelGeneratorTile.getEnergy(TileEntityFurnace.getItemBurnTime(stack)) > 0L && !stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null);

            for (ItemStack fuel: OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.keySet())
                if (stack.isItemEqual(fuel) && !stack.isEmpty())
                    accept = true;

            return accept;
        }
        else
            return !stack.isEmpty() && TileEntityFurnace.isItemFuel(stack) && !stack.getItem().equals(Items.LAVA_BUCKET) && !stack.getItem().equals(ForgeModContainer.getInstance().universalBucket) && PetrifiedFuelGeneratorTile.getEnergy(TileEntityFurnace.getItemBurnTime(stack)) > 0L && !stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null);
    }
}
