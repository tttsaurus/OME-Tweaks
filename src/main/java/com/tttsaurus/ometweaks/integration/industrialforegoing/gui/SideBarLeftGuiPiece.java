package com.tttsaurus.ometweaks.integration.industrialforegoing.gui;

import com.tttsaurus.ometweaks.gui.GuiResources;
import com.tttsaurus.ometweaks.render.ImagePrefab;
import com.tttsaurus.ometweaks.render.RenderUtils;
import net.ndrei.teslacorelib.gui.BasicContainerGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import javax.annotation.Nonnull;

public class SideBarLeftGuiPiece extends BasicContainerGuiPiece
{
    public SideBarLeftGuiPiece(int left, int top, int width, int height)
    {
        super(left, top, width, height);
    }

    @Override
    public void drawBackgroundLayer(@Nonnull BasicTeslaGuiContainer<?> container, int guiX, int guiY, float partialTicks, int mouseX, int mouseY)
    {
        super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);

        float left = getLeft() - 4;
        float top = getTop() - 4;
        float width = getWidth() + 8;
        float height = getHeight() + 8;

        RenderUtils.storeCommonGlStates();
        RenderUtils.renderImagePrefab(guiX + left, guiY + top, width, height, GuiResources.get("vanilla_background"));
        ImagePrefab imagePrefab = GuiResources.get("sidebar_left_component");
        RenderUtils.renderImagePrefab(guiX, guiY + top, imagePrefab.texture2D.getWidth(), height, imagePrefab);
        RenderUtils.restoreCommonGlStates();
    }
}
