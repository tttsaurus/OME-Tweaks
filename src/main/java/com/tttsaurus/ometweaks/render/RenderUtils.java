package com.tttsaurus.ometweaks.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static com.tttsaurus.ometweaks.render.CommonBuffers.FLOAT_BUFFER_16;
import static com.tttsaurus.ometweaks.render.CommonBuffers.INT_BUFFER_16;

public final class RenderUtils
{
    //<editor-fold desc="gl states">
    private static int textureID = 0;
    private static float r = 0, g = 0, b = 0, a = 0;
    private static boolean blend = false;
    private static boolean lighting = false;
    private static boolean texture2D = false;
    private static boolean alphaTest = false;
    private static int shadeModel = 0;
    private static boolean depthTest = false;
    private static boolean cullFace = false;
    private static int blendSrcRgb;
    private static int blendDstRgb;
    private static int blendSrcAlpha;
    private static int blendDstAlpha;
    private static int alphaFunc;
    private static float alphaRef;
    //</editor-fold>

    //<editor-fold desc="gl state management">
    public static void storeCommonGlStates()
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        textureID = INT_BUFFER_16.get(0);
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, FLOAT_BUFFER_16);
        r = FLOAT_BUFFER_16.get(0);
        g = FLOAT_BUFFER_16.get(1);
        b = FLOAT_BUFFER_16.get(2);
        a = FLOAT_BUFFER_16.get(3);
        blend = GL11.glIsEnabled(GL11.GL_BLEND);
        lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        texture2D = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        alphaTest = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        GL11.glGetInteger(GL11.GL_SHADE_MODEL, INT_BUFFER_16);
        shadeModel = INT_BUFFER_16.get(0);
        depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB, INT_BUFFER_16);
        blendSrcRgb = INT_BUFFER_16.get(0);
        GL11.glGetInteger(GL14.GL_BLEND_DST_RGB, INT_BUFFER_16);
        blendDstRgb = INT_BUFFER_16.get(0);
        GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA, INT_BUFFER_16);
        blendSrcAlpha = INT_BUFFER_16.get(0);
        GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA, INT_BUFFER_16);
        blendDstAlpha = INT_BUFFER_16.get(0);
        GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC, INT_BUFFER_16);
        alphaFunc = INT_BUFFER_16.get(0);
        GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF, FLOAT_BUFFER_16);
        alphaRef = FLOAT_BUFFER_16.get(0);
    }

    public static void restoreCommonGlStates()
    {
        GlStateManager.alphaFunc(alphaFunc, alphaRef);
        GlStateManager.tryBlendFuncSeparate(blendSrcRgb, blendDstRgb, blendSrcAlpha, blendDstAlpha);
        if (cullFace)
            GlStateManager.enableCull();
        else
            GlStateManager.disableCull();
        if (depthTest)
            GlStateManager.enableDepth();
        else
            GlStateManager.disableDepth();
        GlStateManager.shadeModel(shadeModel);
        if (alphaTest)
            GlStateManager.enableAlpha();
        else
            GlStateManager.disableAlpha();
        if (texture2D)
            GlStateManager.enableTexture2D();
        else
            GlStateManager.disableTexture2D();
        if (lighting)
            GlStateManager.enableLighting();
        else
            GlStateManager.disableLighting();
        if (blend)
            GlStateManager.enableBlend();
        else
            GlStateManager.disableBlend();
        GlStateManager.color(r, g, b, a);
        GlStateManager.bindTexture(textureID);
    }
    //</editor-fold>

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

    public static void renderRect(float x, float y, float width, float height, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(width, height, 0);
        Gui.drawRect(0, 0, 1, 1, color);
        GlStateManager.popMatrix();
    }

    public static void renderImagePrefab(float x, float y, float width, float height, ImagePrefab imagePrefab)
    {
        switch (imagePrefab.type)
        {
            case TEXTURE_2D -> renderTexture2D(x, y, width, height, imagePrefab.texture2D.getGlTextureID(), -1);
            case NINE_PATCH_BORDER -> renderNinePatchBorder(x, y, width, height, imagePrefab.ninePatchBorder, -1);
        }
    }
    public static void renderImagePrefab(float x, float y, float width, float height, ImagePrefab imagePrefab, int color)
    {
        switch (imagePrefab.type)
        {
            case TEXTURE_2D -> renderTexture2D(x, y, width, height, imagePrefab.texture2D.getGlTextureID(), color);
            case NINE_PATCH_BORDER -> renderNinePatchBorder(x, y, width, height, imagePrefab.ninePatchBorder, color);
        }
    }

    public static void renderNinePatchBorder(float x, float y, float width, float height, NinePatchBorder ninePatchBorder)
    {
        renderNinePatchBorder(x, y, width, height, ninePatchBorder, -1);
    }
    public static void renderNinePatchBorder(float x, float y, float width, float height, NinePatchBorder ninePatchBorder, int color)
    {
        float ppu = 1f;

        float width1, height1;
        if (ninePatchBorder.topLeft.sizeDeductionByPixels)
        {
            width1 = ninePatchBorder.topLeft.tex.getWidth() / ppu;
            height1 = ninePatchBorder.topLeft.tex.getHeight() / ppu;
        }
        else
        {
            width1 = ninePatchBorder.topLeft.width;
            height1 = ninePatchBorder.topLeft.height;
        }
        renderTexture2D(x, y, width1, height1, 1f, 1f, ninePatchBorder.topLeft.tex.getGlTextureID(), color);

        float width2, height2;
        if (ninePatchBorder.topRight.sizeDeductionByPixels)
        {
            width2 = ninePatchBorder.topRight.tex.getWidth() / ppu;
            height2 = ninePatchBorder.topRight.tex.getHeight() / ppu;
        }
        else
        {
            width2 = ninePatchBorder.topRight.width;
            height2 = ninePatchBorder.topRight.height;
        }
        renderTexture2D(x + width - width2, y, width2, height2, 1f, 1f, ninePatchBorder.topRight.tex.getGlTextureID(), color);

        if (width - width1 - width2 > 0)
        {
            float width3, height3;
            if (ninePatchBorder.topCenter.sizeDeductionByPixels)
            {
                width3 = ninePatchBorder.topCenter.tex.getWidth() / ppu;
                height3 = ninePatchBorder.topCenter.tex.getHeight() / ppu;
            }
            else
            {
                width3 = ninePatchBorder.topCenter.width;
                height3 = ninePatchBorder.topCenter.height;
            }
            if (ninePatchBorder.topCenter.tiling)
                renderTexture2D(x + width1, y, width - width1 - width2, height3, (width - width1 - width2) / width3, 1f, ninePatchBorder.topCenter.tex.getGlTextureID(), color);
            else
                renderTexture2D(x + width1, y, width - width1 - width2, height3, 1f, 1f, ninePatchBorder.topCenter.tex.getGlTextureID(), color);
        }

        float width4, height4;
        if (ninePatchBorder.bottomLeft.sizeDeductionByPixels)
        {
            width4 = ninePatchBorder.bottomLeft.tex.getWidth() / ppu;
            height4 = ninePatchBorder.bottomLeft.tex.getHeight() / ppu;
        }
        else
        {
            width4 = ninePatchBorder.bottomLeft.width;
            height4 = ninePatchBorder.bottomLeft.height;
        }
        renderTexture2D(x, y + height - height4, width4, height4, 1f, 1f, ninePatchBorder.bottomLeft.tex.getGlTextureID(), color);

        float width5, height5;
        if (ninePatchBorder.bottomRight.sizeDeductionByPixels)
        {
            width5 = ninePatchBorder.bottomRight.tex.getWidth() / ppu;
            height5 = ninePatchBorder.bottomRight.tex.getHeight() / ppu;
        }
        else
        {
            width5 = ninePatchBorder.bottomRight.width;
            height5 = ninePatchBorder.bottomRight.height;
        }
        renderTexture2D(x + width - width5, y + height - height5, width5, height5, 1f, 1f, ninePatchBorder.bottomRight.tex.getGlTextureID(), color);

        if (width - width4 - width5 > 0)
        {
            float width6, height6;
            if (ninePatchBorder.bottomCenter.sizeDeductionByPixels)
            {
                width6 = ninePatchBorder.bottomCenter.tex.getWidth() / ppu;
                height6 = ninePatchBorder.bottomCenter.tex.getHeight() / ppu;
            }
            else
            {
                width6 = ninePatchBorder.bottomCenter.width;
                height6 = ninePatchBorder.bottomCenter.height;
            }
            if (ninePatchBorder.bottomCenter.tiling)
                renderTexture2D(x + width4, y + height - height6, width - width4 - width5, height6, (width - width4 - width5) / width6, 1f, ninePatchBorder.bottomCenter.tex.getGlTextureID(), color);
            else
                renderTexture2D(x + width4, y + height - height6, width - width4 - width5, height6, 1f, 1f, ninePatchBorder.bottomCenter.tex.getGlTextureID(), color);
        }

        if (height - height1 - height4 > 0)
        {
            float width7, height7;
            if (ninePatchBorder.centerLeft.sizeDeductionByPixels)
            {
                width7 = ninePatchBorder.centerLeft.tex.getWidth() / ppu;
                height7 = ninePatchBorder.centerLeft.tex.getHeight() / ppu;
            }
            else
            {
                width7 = ninePatchBorder.centerLeft.width;
                height7 = ninePatchBorder.centerLeft.height;
            }
            if (ninePatchBorder.centerLeft.tiling)
                renderTexture2D(x, y + height1, width7, height - height1 - height4, 1f, (height - height1 - height4) / height7, ninePatchBorder.centerLeft.tex.getGlTextureID(), color);
            else
                renderTexture2D(x, y + height1, width7, height - height1 - height4, 1f, 1f, ninePatchBorder.centerLeft.tex.getGlTextureID(), color);
        }

        if (height - height2 - height5 > 0)
        {
            float width8, height8;
            if (ninePatchBorder.centerRight.sizeDeductionByPixels)
            {
                width8 = ninePatchBorder.centerRight.tex.getWidth() / ppu;
                height8 = ninePatchBorder.centerRight.tex.getHeight() / ppu;
            }
            else
            {
                width8 = ninePatchBorder.centerRight.width;
                height8 = ninePatchBorder.centerRight.height;
            }
            if (ninePatchBorder.centerRight.tiling)
                renderTexture2D(x + width - width8, y + height2, width8, height - height2 - height5, 1f, (height - height2 - height5) / height8, ninePatchBorder.centerRight.tex.getGlTextureID(), color);
            else
                renderTexture2D(x + width - width8, y + height2, width8, height - height2 - height5, 1f, 1f, ninePatchBorder.centerRight.tex.getGlTextureID(), color);
        }

        if (width - width1 - width2 > 0 && height - height1 - height4 > 0)
        {
            float width9, height9;
            if (ninePatchBorder.center.sizeDeductionByPixels)
            {
                width9 = ninePatchBorder.center.tex.getWidth() / ppu;
                height9 = ninePatchBorder.center.tex.getHeight() / ppu;
            }
            else
            {
                width9 = ninePatchBorder.center.width;
                height9 = ninePatchBorder.center.height;
            }
            if (ninePatchBorder.center.tiling)
                renderTexture2D(x + width1, y + height1, width - width1 - width2, height - height1 - height4, (width - width1 - width2) / width9, (height - height1 - height4) / height9, ninePatchBorder.center.tex.getGlTextureID(), color);
            else
                renderTexture2D(x + width1, y + height1, width - width1 - width2, height - height1 - height4, 1f, 1f, ninePatchBorder.center.tex.getGlTextureID(), color);
        }
    }
    public static void renderTexture2D(float x, float y, float width, float height, int textureId)
    {
        renderTexture2D(x, y, width, height, 1f, 1f, textureId);
    }
    public static void renderTexture2D(float x, float y, float width, float height, int textureId, int color)
    {
        renderTexture2D(x, y, width, height, 1f, 1f, textureId, color);
    }
    public static void renderTexture2D(float x, float y, float width, float height, float widthTiling, float heightTiling, int textureId)
    {
        renderTexture2D(x, y, width, height, widthTiling, heightTiling, textureId, -1);
    }
    public static void renderTexture2D(float x, float y, float width, float height, float widthTiling, float heightTiling, int textureId, int color)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        int textureID = INT_BUFFER_16.get(0);

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(r, g, b, a);

        GlStateManager.bindTexture(textureId);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(0, heightTiling).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex(widthTiling, heightTiling).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex(widthTiling, 0).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(0, 0).endVertex();
        tessellator.draw();

        GlStateManager.bindTexture(textureID);
    }
    public static void renderTexture2DFullScreen(int textureId)
    {
        renderTexture2DFullScreen(textureId, 1f, 1f);
    }
    public static void renderTexture2DFullScreen(int textureId, int color)
    {
        renderTexture2DFullScreen(textureId, 1f, 1f, color);
    }
    public static void renderTexture2DFullScreen(int textureId, float widthTiling, float heightTiling)
    {
        renderTexture2DFullScreen(textureId, widthTiling, heightTiling, -1);
    }
    public static void renderTexture2DFullScreen(int textureId, float widthTiling, float heightTiling, int color)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        int textureID = INT_BUFFER_16.get(0);

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double width = resolution.getScaledWidth_double();
        double height = resolution.getScaledHeight_double();

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(r, g, b, a);

        GlStateManager.bindTexture(textureId);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(0, height, zLevel).tex(0, heightTiling).endVertex();
        buffer.pos(width, height, zLevel).tex(widthTiling, heightTiling).endVertex();
        buffer.pos(width, 0, zLevel).tex(widthTiling, 0).endVertex();
        buffer.pos(0, 0, zLevel).tex(0, 0).endVertex();
        tessellator.draw();

        GlStateManager.bindTexture(textureID);
    }

    @Nullable
    public static Texture2D createTexture2D(BufferedImage bufferedImage)
    {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if (width == 0 || height == 0) return null;

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        int[] pixels = new int[width * height];
        bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));  // r
                buffer.put((byte) ((pixel >> 8) & 0xFF));   // g
                buffer.put((byte) (pixel & 0xFF));          // b
                buffer.put((byte) ((pixel >> 24) & 0xFF));  // a
            }
        }
        buffer.flip();

        return new Texture2D(width, height, buffer);
    }
}
