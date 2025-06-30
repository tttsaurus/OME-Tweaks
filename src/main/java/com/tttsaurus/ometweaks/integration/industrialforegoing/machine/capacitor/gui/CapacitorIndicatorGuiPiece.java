package com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.gui;

import com.tttsaurus.ometweaks.gui.GuiResources;
import com.tttsaurus.ometweaks.render.ImagePrefab;
import com.tttsaurus.ometweaks.render.RenderUtils;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.gui.BasicContainerGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import javax.annotation.Nonnull;

public class CapacitorIndicatorGuiPiece extends BasicContainerGuiPiece
{
    private final ItemStackHandler itemStackHandler;

    public CapacitorIndicatorGuiPiece(int left, int top, ItemStackHandler itemStackHandler)
    {
        super(left, top, 0, 0);
        this.itemStackHandler = itemStackHandler;
    }

    @Override
    public void drawBackgroundLayer(@Nonnull BasicTeslaGuiContainer<?> container, int guiX, int guiY, float partialTicks, int mouseX, int mouseY)
    {
        super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);

        //if (itemStackHandler.getStackInSlot(0).isEmpty())
        {
            RenderUtils.storeCommonGlStates();

            ImagePrefab capLogo = GuiResources.get("capacitor_logo");
            RenderUtils.renderImagePrefab(guiX + getLeft() + 2, guiY + getTop() + 2, capLogo.texture2D.getWidth(), capLogo.texture2D.getHeight(), capLogo);

            RenderUtils.restoreCommonGlStates();
        }
    }
}
