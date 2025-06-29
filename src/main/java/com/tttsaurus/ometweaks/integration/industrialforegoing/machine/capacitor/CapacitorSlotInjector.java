package com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor;

import com.tttsaurus.ometweaks.function.IAction_2Param;
import com.tttsaurus.ometweaks.function.IAction_3Param;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.gui.SideBarLeftGuiPiece;
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
import net.ndrei.teslacorelib.gui.TiledRenderedGuiPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.ColoredItemHandler;
import net.ndrei.teslacorelib.tileentities.ElectricMachine;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;

public class CapacitorSlotInjector
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
                CapacitorSlotInjector.addInventory = (arg0, arg1) ->
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
                CapacitorSlotInjector.addInventoryToStorage = (arg0, arg1, arg2) ->
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
                pieces.add(new SideBarLeftGuiPiece(box.getLeft(), box.getTop(), 18, 18));
                pieces.add(new TiledRenderedGuiPiece(box.getLeft(), box.getTop(), 18, 18, 1, 1, BasicTeslaGuiContainer.Companion.getMACHINE_BACKGROUND(), 108, 225, EnumDyeColor.GRAY));
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
