package com.tttsaurus.ometweaks.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;

import static com.tttsaurus.ometweaks.render.CommonBuffers.INT_BUFFER_16;

public final class Texture2D implements IGlDisposable
{
    private int glTextureID = 0;
    private final int width;
    private final int height;
    private boolean isGlRegistered;

    public int getGlTextureID() { return glTextureID; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isGlRegistered() { return isGlRegistered; }

    public Texture2D(int width, int height, ByteBuffer byteBuffer)
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        int textureID = INT_BUFFER_16.get(0);

        glTextureID = GL11.glGenTextures();
        this.width = width;
        this.height = height;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);

        isGlRegistered = true;

        GlStateManager.bindTexture(textureID);

        GlResourceManager.addDisposable(this);
    }

    public void dispose()
    {
        if (glTextureID != 0) GL11.glDeleteTextures(glTextureID);
        glTextureID = 0;
        isGlRegistered = false;
        GlResourceManager.removeDisposable(this);
    }
}
