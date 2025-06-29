package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.tile.WorkingAreaElectricMachine;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.CapacitorSlotInjector;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.gui.CapacitorIndicatorGuiPiece;
import crazypants.enderio.api.capacitor.CapabilityCapacitorData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.util.List;

@Mixin(CustomElectricMachine.class)
public class CustomElectricMachineMixin
{
    @Unique
    private ItemStackHandler OME_Tweaks$capacitorStackHandler;

    @WrapMethod(method = "protectedUpdate", remap = false)
    public void protectedUpdate(Operation<Float> original)
    {
        ItemStack itemStack = OME_Tweaks$capacitorStackHandler.getStackInSlot(0);

        if (itemStack.isEmpty()) return;
        if (!itemStack.hasCapability(CapabilityCapacitorData.getCapNN(), null)) return;

        original.call();
    }

    @WrapMethod(method = "initializeInventories", remap = false)
    public void extraInventory(Operation<Void> original)
    {
        original.call();
        OME_Tweaks$capacitorStackHandler = CapacitorSlotInjector.addCapacitorInventory((WorkingAreaElectricMachine)(Object)this);
    }

    @WrapMethod(method = "getGuiContainerPieces", remap = false)
    public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer<?> container, Operation<List<IGuiContainerPiece>> original)
    {
        List<IGuiContainerPiece> list = original.call(container);
        list.add(new CapacitorIndicatorGuiPiece(-15, 60));
        return list;
    }
}
