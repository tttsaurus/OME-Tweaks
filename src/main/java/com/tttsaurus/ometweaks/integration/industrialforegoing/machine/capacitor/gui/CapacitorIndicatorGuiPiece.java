package com.tttsaurus.ometweaks.integration.industrialforegoing.machine.capacitor.gui;

import com.tttsaurus.ometweaks.gui.GuiResources;
import com.tttsaurus.ometweaks.render.ImagePrefab;
import com.tttsaurus.ometweaks.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.gui.BasicContainerGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import org.apache.commons.lang3.time.StopWatch;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CapacitorIndicatorGuiPiece extends BasicContainerGuiPiece
{
    private static final Minecraft MC = Minecraft.getMinecraft();

    private final ItemStackHandler itemStackHandler;
    private final StopWatch stopWatch;
    private int animIndex = 0;

    public CapacitorIndicatorGuiPiece(int left, int top, ItemStackHandler itemStackHandler)
    {
        super(left, top, 0, 0);
        this.itemStackHandler = itemStackHandler;
        stopWatch = new StopWatch();
    }

    @Override
    public void drawBackgroundLayer(@Nonnull BasicTeslaGuiContainer<?> container, int guiX, int guiY, float partialTicks, int mouseX, int mouseY)
    {
        super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);

        RenderUtils.storeCommonGlStates();

        ImagePrefab capLogo = GuiResources.get("capacitor_logo");
        RenderUtils.renderImagePrefab(guiX + getLeft() + 2, guiY + getTop() + 2, capLogo.texture2D.getWidth(), capLogo.texture2D.getHeight(), capLogo);

        if (itemStackHandler.getStackInSlot(0).isEmpty())
        {
            if (!stopWatch.isStarted()) stopWatch.start();
            if (stopWatch.getNanoTime() / 1e9d > 0.18d)
            {
                animIndex = animIndex + 1 > 3 ? 0 : animIndex + 1;
                stopWatch.stop();
                stopWatch.reset();
                stopWatch.start();
            }

            ImagePrefab arrow = GuiResources.get("indicator_arrow_" + animIndex);
            RenderUtils.renderImagePrefab(guiX + getLeft() + 6, guiY + getTop() - 7 + animIndex, arrow.texture2D.getWidth(), arrow.texture2D.getHeight(), arrow);

            if (mouseX >= guiX + getLeft() && mouseY >= guiY + getTop() && mouseX < guiX + getLeft() + 18 && mouseY < guiY + getTop() + 18)
            {
                List<String> list = new ArrayList<>();
                String line1 = I18n.format("enderio.gui.generic.nocap.line1");
                String line2 = I18n.format("enderio.gui.generic.nocap.line2");
                String line3 = I18n.format("enderio.gui.generic.nocap.line3");
                list.add(line1);
                list.add(line2);
                list.add(line3);

                int width = Math.max(MC.fontRenderer.getStringWidth(line1), Math.max(MC.fontRenderer.getStringWidth(line2), MC.fontRenderer.getStringWidth(line3))) + 1;

                ScaledResolution resolution = new ScaledResolution(MC);
                GuiUtils.drawHoveringText(list, guiX + getLeft() - width, guiY + getTop() - 22, resolution.getScaledWidth(), resolution.getScaledHeight(), width, MC.fontRenderer);
            }
        }

        RenderUtils.restoreCommonGlStates();
    }
}
