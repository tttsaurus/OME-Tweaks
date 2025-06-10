package com.tttsaurus.ometweaks.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public final class RenderUtils
{
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static float zLevel = 0;

    private static void addArcVertices(BufferBuilder bufferbuilder, float cx, float cy, float radius, float startAngle, float endAngle, int segments)
    {
        startAngle -= 90;
        endAngle -= 90;
        float x = (float)(cx + Math.cos(Math.toRadians(startAngle)) * radius);
        float y = (float)(cy + Math.sin(Math.toRadians(startAngle)) * radius);
        for (int i = 1; i <= segments; i++)
        {
            float angle = (float)Math.toRadians(startAngle + (endAngle - startAngle) * i / segments);
            float dx = (float)(cx + Math.cos(angle) * radius);
            float dy = (float)(cy + Math.sin(angle) * radius);
            bufferbuilder.pos(x, y, 0).endVertex();
            bufferbuilder.pos(dx, dy, 0).endVertex();
            x = dx;
            y = dy;
        }
    }

    public static void renderText(String text, float x, float y, float scale, int color, boolean shadow)
    {
        GlStateManager.disableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(scale, scale, 0);
        fontRenderer.drawString(text, 0, 0, color, shadow);
        GlStateManager.popMatrix();
    }

    public static void renderRoundedRect(float x, float y, float width, float height, float radius, int color)
    {
        int segments = Math.max(5, (int)(radius / 2f));
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(r, g, b, a);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, zLevel);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);

        addArcVertices(bufferbuilder, x + width - radius, y + radius, radius, 0, 90, segments);
        bufferbuilder.pos(x + width, y + radius, 0).endVertex();
        bufferbuilder.pos(x + width, y + height - radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + width - radius, y + height - radius, radius, 90, 180, segments);
        bufferbuilder.pos(x + width - radius, y + height, 0).endVertex();
        bufferbuilder.pos(x + radius, y + height, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + height - radius, radius, 180, 270, segments);
        bufferbuilder.pos(x, y + height - radius, 0).endVertex();
        bufferbuilder.pos(x, y + radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + radius, radius, 270, 360, segments);
        bufferbuilder.pos(x + radius, y, 0).endVertex();
        bufferbuilder.pos(x + width - radius, y, 0).endVertex();

        tessellator.draw();

        GlStateManager.popMatrix();
    }
}
