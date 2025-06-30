package com.tttsaurus.ometweaks.integration.industrialforegoing.machine;

import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;
import java.util.List;

public interface IMachineGuiContainer
{
    Class<? extends SidedTileEntity> getMachineType();
    List<IGuiContainerPiece> getGuiPieces();
}
