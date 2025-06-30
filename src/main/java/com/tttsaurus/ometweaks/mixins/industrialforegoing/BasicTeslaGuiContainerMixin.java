package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.IMachineGuiContainer;
import net.minecraft.inventory.Container;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(BasicTeslaGuiContainer.class)
public class BasicTeslaGuiContainerMixin implements IMachineGuiContainer
{
    @Unique
    public Class<? extends SidedTileEntity> tileEntityClass;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void onConstruct(int guiId, Container container, SidedTileEntity entity, CallbackInfo ci)
    {
        tileEntityClass = entity.getClass();
    }

    @Override
    public Class<? extends SidedTileEntity> getMachineType()
    {
        return tileEntityClass;
    }

    @Shadow
    private List<IGuiContainerPiece> pieces;

    @Override
    public List<IGuiContainerPiece> getGuiPieces()
    {
        return pieces;
    }
}
