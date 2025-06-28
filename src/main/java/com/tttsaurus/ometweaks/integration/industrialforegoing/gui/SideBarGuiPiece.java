package com.tttsaurus.ometweaks.integration.industrialforegoing.gui;

import com.tttsaurus.ometweaks.utils.RenderUtils;
import net.ndrei.teslacorelib.gui.BasicContainerGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SideBarGuiPiece extends BasicContainerGuiPiece
{
    public SideBarGuiPiece(int left, int top, int width, int height)
    {
        super(left, top, width, height);
    }

    private static final FloatBuffer FLOAT_BUFFER_16 = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();

    @Override
    public void drawBackgroundLayer(@NotNull BasicTeslaGuiContainer<?> container, int guiX, int guiY, float partialTicks, int mouseX, int mouseY)
    {
        super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);

        RenderUtils.storeCommonGlStates();

        RenderUtils.renderRoundedRect(guiX + getLeft(), guiY + getTop(), getWidth(), getHeight(), 3f, Color.GREEN.getRGB());

        RenderUtils.restoreCommonGlStates();
    }
}
