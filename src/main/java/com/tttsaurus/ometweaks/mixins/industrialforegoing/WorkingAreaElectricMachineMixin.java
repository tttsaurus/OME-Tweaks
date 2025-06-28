package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.WorkingAreaElectricMachine;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.gui.SideBarGuiPiece;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.containers.BasicTeslaContainer;
import net.ndrei.teslacorelib.containers.FilteredSlot;
import net.ndrei.teslacorelib.gui.*;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.ColoredItemHandler;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.lang.reflect.Method;
import java.util.List;

@Mixin(WorkingAreaElectricMachine.class)
public class WorkingAreaElectricMachineMixin
{
    @Unique
    private ItemStackHandler OME_Tweaks$capacitorStackHandler;

    @WrapMethod(method = "performWork", remap = false)
    protected float performWork(Operation<Float> original)
    {
        ItemStack itemStack = OME_Tweaks$capacitorStackHandler.getStackInSlot(0);
        if (itemStack.isEmpty()) return 0f;
        return original.call();
    }

    @WrapMethod(method = "initializeInventories", remap = false)
    public void extraInventory(Operation<Void> original)
    {
        original.call();

        WorkingAreaElectricMachine this0 = (WorkingAreaElectricMachine)(Object)this;

        Method addInventory = null;
        try
        {
            addInventory = SidedTileEntity.class.getDeclaredMethod("addInventory", IItemHandler.class);
        }
        catch (NoSuchMethodException ignored) { }
        Method addInventoryToStorage = null;
        try
        {
            addInventoryToStorage = SidedTileEntity.class.getDeclaredMethod("addInventoryToStorage", ItemStackHandler.class, String.class);
        }
        catch (NoSuchMethodException ignored) { }

        OME_Tweaks$capacitorStackHandler = new ItemStackHandler(1)
        {
            protected void onContentsChanged(int slot)
            {
                this0.markDirty();
            }
        };

        try
        {
            addInventory.invoke(this0, new ColoredItemHandler(OME_Tweaks$capacitorStackHandler, EnumDyeColor.GREEN, "capacitor", new BoundingRectangle(-15, 60, 18, 18))
            {
                public boolean canInsertItem(int slot, ItemStack stack)
                {
                    return true;
                }

                public boolean canExtractItem(int slot)
                {
                    return true;
                }

                public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container)
                {
                    List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);
                    BoundingRectangle box = this.getBoundingBox();
                    pieces.add(new SideBarGuiPiece(box.getLeft() - 2, box.getTop() - 2, 22, 22));
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
        }
        catch (Exception ignored) { }

        try
        {
            addInventoryToStorage.invoke(this0, OME_Tweaks$capacitorStackHandler, "capacitor_slot");
        }
        catch (Exception ignored) { }
    }

    @Unique
    private List<IGuiContainerPiece> OME_Tweaks$tempGuiList;

    @WrapOperation(
            method = "getGuiContainerPieces",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/buuz135/industrial/tile/CustomElectricMachine;getGuiContainerPieces(Lnet/ndrei/teslacorelib/gui/BasicTeslaGuiContainer;)Ljava/util/List;"
            ),
            remap = false)
    public List<IGuiContainerPiece> getGuiContainerPieces(WorkingAreaElectricMachine instance, BasicTeslaGuiContainer<?> container, Operation<List<IGuiContainerPiece>> original)
    {
        OME_Tweaks$tempGuiList = original.call(instance, container);
        return OME_Tweaks$tempGuiList;
    }

    @Inject(method = "getGuiContainerPieces", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER), remap = false)
    public void extraGuiPieces(BasicTeslaGuiContainer container, CallbackInfoReturnable<List<IGuiContainerPiece>> cir)
    {
        OME_Tweaks$tempGuiList.add(new SideBarGuiPiece(-15, 80, 18, 6));
    }
}
