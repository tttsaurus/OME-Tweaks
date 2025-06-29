package com.tttsaurus.ometweaks.integration.industrialforegoing.gui;

import com.tttsaurus.ometweaks.gui.GuiResources;
import com.tttsaurus.ometweaks.render.RenderUtils;
import net.ndrei.teslacorelib.gui.BasicContainerGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import javax.annotation.Nonnull;

public class SideBarGuiPiece extends BasicContainerGuiPiece
{
    public SideBarGuiPiece(int left, int top, int width, int height)
    {
        super(left, top, width, height);
    }

    @Override
    public void drawBackgroundLayer(@Nonnull BasicTeslaGuiContainer<?> container, int guiX, int guiY, float partialTicks, int mouseX, int mouseY)
    {
        super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);

        RenderUtils.storeCommonGlStates();
        RenderUtils.renderImagePrefab(guiX + getLeft(), guiY + getTop(), getWidth(), getHeight(), GuiResources.get("vanilla_background"));
        RenderUtils.restoreCommonGlStates();
    }
}
