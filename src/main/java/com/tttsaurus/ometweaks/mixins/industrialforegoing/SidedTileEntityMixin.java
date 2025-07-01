package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.function.IFunc;
import com.tttsaurus.ometweaks.integration.enderio.EnderIOModule;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.IMachineWorldProvider;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.IMachineWithCapacitor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

@Mixin(SidedTileEntity.class)
public class SidedTileEntityMixin implements IMachineWorldProvider
{
    @WrapMethod(method = "isPaused", remap = false)
    public boolean isPaused(Operation<Boolean> original)
    {
        if (!IndustrialForegoingModule.ENABLE_IF_EIO_CAPACITOR_COMPAT || !EnderIOModule.IS_MOD_LOADED) return original.call();

        SidedTileEntity this0 = (SidedTileEntity)(Object)this;

        if (this0 instanceof IMachineWithCapacitor machine)
        {
            ItemStackHandler handler = machine.getCapacitorStackHandler();

            if (handler == null) return true;

            ItemStack itemStack = handler.getStackInSlot(0);

            if (itemStack.isEmpty()) return true;

            return original.call();
        }
        else
            return original.call();
    }

    @Unique
    public IFunc<World> OME_Tweaks$worldGetter;

    @Override
    public World getWorld()
    {
        if (OME_Tweaks$worldGetter == null)
        {
            SidedTileEntity this0 = (SidedTileEntity)(Object)this;

            Field world = null;
            try
            {
                world = TileEntity.class.getDeclaredField("world");
            }
            catch (Exception ignored)
            {
                try
                {
                    world = TileEntity.class.getDeclaredField("field_145850_b");
                }
                catch (Exception ignored2) { }
            }

            try
            {
                world.setAccessible(true);
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                MethodHandle handle = lookup.unreflectGetter(world);
                OME_Tweaks$worldGetter = () ->
                {
                    try
                    {
                        return (World)handle.invoke(this0);
                    }
                    catch (Throwable ignored) { return null; }
                };
            }
            catch (Exception ignored) { }
        }

        return OME_Tweaks$worldGetter.invoke();
    }
}
