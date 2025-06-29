package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.tile.WorkingAreaElectricMachine;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.CapacitorInventoryInjector;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.IMachineWithCapacitor;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.gui.CapacitorIndicatorGuiPiece;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.util.List;

@Mixin(CustomElectricMachine.class)
public class CustomElectricMachineMixin implements IMachineWithCapacitor
{
    @Unique
    private ItemStackHandler OME_Tweaks$capacitorStackHandler;

    @Override
    public ItemStackHandler getCapacitorStackHandler()
    {
        return OME_Tweaks$capacitorStackHandler;
    }

    @WrapMethod(method = "initializeInventories", remap = false)
    public void extraInventory(Operation<Void> original)
    {
        original.call();
        OME_Tweaks$capacitorStackHandler = CapacitorInventoryInjector.addCapacitorInventory((WorkingAreaElectricMachine)(Object)this);
    }

    @WrapMethod(method = "getGuiContainerPieces", remap = false)
    public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer<?> container, Operation<List<IGuiContainerPiece>> original)
    {
        List<IGuiContainerPiece> list = original.call(container);
        list.add(new CapacitorIndicatorGuiPiece(-15, 60));
        return list;
    }
}
