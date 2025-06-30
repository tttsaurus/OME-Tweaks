package com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.gui;

import com.tttsaurus.ometweaks.gui.GuiResources;
import com.tttsaurus.ometweaks.integration.jei.IJEIExclusionAreaGuiPiece;
import com.tttsaurus.ometweaks.render.ImagePrefab;
import com.tttsaurus.ometweaks.render.RenderUtils;
import net.ndrei.teslacorelib.gui.BasicContainerGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import javax.annotation.Nonnull;

public class CapacitorSideBarGuiPiece extends BasicContainerGuiPiece implements IJEIExclusionAreaGuiPiece
{
    public CapacitorSideBarGuiPiece(int left, int top, int width, int height)
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

        ImagePrefab bg = GuiResources.get("vanilla_background");
        RenderUtils.renderImagePrefab(guiX + left, guiY + top, width, height, bg);
        ImagePrefab sidebar = GuiResources.get("sidebar_left_component");
        RenderUtils.renderImagePrefab(guiX, guiY + top, sidebar.texture2D.getWidth(), height, sidebar);

        float width1, height1;
        if (bg.ninePatchBorder.topLeft.sizeDeductionByPixels)
        {
            width1 = bg.ninePatchBorder.topLeft.tex.getWidth();
            height1 = bg.ninePatchBorder.topLeft.tex.getHeight();
        }
        else
        {
            width1 = bg.ninePatchBorder.topLeft.width;
            height1 = bg.ninePatchBorder.topLeft.height;
        }

        float height4;
        if (bg.ninePatchBorder.bottomLeft.sizeDeductionByPixels)
            height4 = bg.ninePatchBorder.bottomLeft.tex.getHeight();
        else
            height4 = bg.ninePatchBorder.bottomLeft.height;

        RenderUtils.renderTexture2D(guiX + left + width1, guiY + top + height1 - 2, width - width1, height - height1 - height4 + 4, bg.ninePatchBorder.center.tex.getGlTextureID());

        RenderUtils.restoreCommonGlStates();
    }

    @Override
    public int exclusionAreaX()
    {
        return getLeft() - 4;
    }

    @Override
    public int exclusionAreaY()
    {
        return getTop() - 4;
    }

    @Override
    public int exclusionAreaWidth()
    {
        return getWidth() + 8;
    }

    @Override
    public int exclusionAreaHeight()
    {
        return getHeight() + 8;
    }
}
