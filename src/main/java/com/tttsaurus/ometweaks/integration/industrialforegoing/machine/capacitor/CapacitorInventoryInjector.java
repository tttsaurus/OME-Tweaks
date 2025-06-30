package com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor;

import com.tttsaurus.ometweaks.function.IAction_2Param;
import com.tttsaurus.ometweaks.function.IAction_3Param;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.IMachineEnergyStorageProvider;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.gui.CapacitorIndicatorGuiPiece;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.gui.CapacitorSideBarGuiPiece;
import crazypants.enderio.api.capacitor.CapabilityCapacitorData;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.containers.BasicTeslaContainer;
import net.ndrei.teslacorelib.containers.FilteredSlot;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.ColoredItemHandler;
import net.ndrei.teslacorelib.inventory.EnergyStorage;
import net.ndrei.teslacorelib.tileentities.ElectricMachine;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;
import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;

public class CapacitorInventoryInjector
{
    private static boolean isFuncsInit = false;
    private static IAction_2Param<SidedTileEntity, IItemHandler> addInventory;
    private static IAction_3Param<SidedTileEntity, ItemStackHandler, String> addInventoryToStorage;

    private static void initFuncs()
    {
        if (!isFuncsInit)
        {
            isFuncsInit = true;

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            try
            {
                Method addInventory = SidedTileEntity.class.getDeclaredMethod("addInventory", IItemHandler.class);
                addInventory.setAccessible(true);
                MethodHandle handle = lookup.unreflect(addInventory);
                CapacitorInventoryInjector.addInventory = (arg0, arg1) ->
                {
                    try
                    {
                        handle.invoke(arg0, arg1);
                    }
                    catch (Throwable ignored) { }
                };
            }
            catch (Exception ignored) { }

            try
            {
                Method addInventoryToStorage = SidedTileEntity.class.getDeclaredMethod("addInventoryToStorage", ItemStackHandler.class, String.class);
                addInventoryToStorage.setAccessible(true);
                MethodHandle handle = lookup.unreflect(addInventoryToStorage);
                CapacitorInventoryInjector.addInventoryToStorage = (arg0, arg1, arg2) ->
                {
                    try
                    {
                        handle.invoke(arg0, arg1, arg2);
                    }
                    catch (Throwable ignored) { }
                };
            }
            catch (Exception ignored) { }
        }
    }

    public static ItemStackHandler addCapacitorInventory(ElectricMachine machine)
    {
        initFuncs();

        ItemStackHandler itemStackHandler = new ItemStackHandler(1)
        {
            @Override
            public @Nonnull ItemStack extractItem(int slot, int amount, boolean simulate)
            {
                if (IndustrialForegoingModule.ENABLE_IF_EIO_CAPACITOR_ON_REMOVE_CLEAR_ENERGY)
                {
                    IMachineEnergyStorageProvider provider = (IMachineEnergyStorageProvider)machine;
                    EnergyStorage energyStorage = provider.getEnergyStorage();
                    if (energyStorage != null)
                        energyStorage.takePower(Long.MAX_VALUE);
                }
                return super.extractItem(slot, amount, simulate);
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                machine.markDirty();
            }

            @Override
            public int getSlotLimit(int slot)
            {
                return 1;
            }
        };

        addInventory.invoke(machine, new ColoredItemHandler(itemStackHandler, EnumDyeColor.PURPLE, "capacitor", new BoundingRectangle(-15, 60, 18, 18))
        {
            public boolean canInsertItem(int slot, ItemStack stack)
            {
                return stack.hasCapability(CapabilityCapacitorData.getCapNN(), null);
            }

            public boolean canExtractItem(int slot)
            {
                return true;
            }

            public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container)
            {
                List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);
                BoundingRectangle box = this.getBoundingBox();

                int left = box.getLeft();
                int top = box.getTop();
                pieces.add(new CapacitorSideBarGuiPiece(left, top, 18, 24));
                pieces.add(new CapacitorIndicatorGuiPiece(left, top, itemStackHandler));

                return pieces;
            }

            public List<Slot> getSlots(BasicTeslaContainer container)
            {
                List<Slot> slots = super.getSlots(container);
                BoundingRectangle boundingRectangle = this.getBoundingBox();

                for(int y = 0; y < this.getInnerHandler().getSlots(); ++y)
                    slots.add(new FilteredSlot(this.getItemHandlerForContainer(), y, boundingRectangle.getLeft() + 1, boundingRectangle.getTop() + 1 + y * 18));

                return slots;
            }
        });

        addInventoryToStorage.invoke(machine, itemStackHandler, "capacitor_slot");

        return itemStackHandler;
    }
}
