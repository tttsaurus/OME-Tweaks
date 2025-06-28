package com.tttsaurus.ometweaks.integration.industrialforegoing.gui;

import net.ndrei.teslacorelib.gui.BasicContainerGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import javax.annotation.Nonnull;

public class CapacitorIndicatorGuiPiece extends BasicContainerGuiPiece
{
    public CapacitorIndicatorGuiPiece(int left, int top)
    {
        super(left, top, 0, 0);
    }

    @Override
    public void drawBackgroundLayer(@Nonnull BasicTeslaGuiContainer<?> container, int guiX, int guiY, float partialTicks, int mouseX, int mouseY)
    {
        super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);

    }
}
