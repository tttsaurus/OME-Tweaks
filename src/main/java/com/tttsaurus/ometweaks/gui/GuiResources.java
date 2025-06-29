package com.tttsaurus.ometweaks.gui;

import com.tttsaurus.ometweaks.render.ImagePrefab;
import com.tttsaurus.ometweaks.render.NinePatchBorder;
import com.tttsaurus.ometweaks.render.RenderUtils;
import com.tttsaurus.ometweaks.render.Texture2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class GuiResources
{
    private static final Map<String, ImagePrefab> imagePrefabs = new HashMap<>();

    public static void register(String name, ImagePrefab imagePrefab)
    {
        imagePrefabs.put(name, imagePrefab);
    }

    public static ImagePrefab get(String name)
    {
        ImagePrefab imagePrefab = imagePrefabs.get(name);
        if (imagePrefab == null)
            return missingTexture;
        else
            return imagePrefab;
    }

    private static ImagePrefab missingTexture;

    @Nullable
    private static BufferedImage getBufferedImageFromRl(ResourceLocation rl)
    {
        try
        {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
            InputStream inputStream = resource.getInputStream();
            return ImageIO.read(inputStream);
        }
        catch (Exception ignored) { }
        return null;
    }

    public static void init()
    {
        Texture2D mcVanillaBgTopLeft = null;
        Texture2D mcVanillaBgTopCenter = null;
        Texture2D mcVanillaBgTopRight = null;
        Texture2D mcVanillaBgCenterLeft = null;
        Texture2D mcVanillaBgCenter = null;
        Texture2D mcVanillaBgCenterRight = null;
        Texture2D mcVanillaBgBottomLeft = null;
        Texture2D mcVanillaBgBottomCenter = null;
        Texture2D mcVanillaBgBottomRight = null;

        BufferedImage image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/top_left.png"));
        if (image != null)
            mcVanillaBgTopLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/top_center.png"));
        if (image != null)
            mcVanillaBgTopCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/top_right.png"));
        if (image != null)
            mcVanillaBgTopRight = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/center_left.png"));
        if (image != null)
            mcVanillaBgCenterLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/center.png"));
        if (image != null)
            mcVanillaBgCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/center_right.png"));
        if (image != null)
            mcVanillaBgCenterRight = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/bottom_left.png"));
        if (image != null)
            mcVanillaBgBottomLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/bottom_center.png"));
        if (image != null)
            mcVanillaBgBottomCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/background/vanilla/bottom_right.png"));
        if (image != null)
            mcVanillaBgBottomRight = RenderUtils.createTexture2D(image);

        ImagePrefab mcVanillaBg = new ImagePrefab(new NinePatchBorder(
                mcVanillaBgTopLeft,
                mcVanillaBgTopCenter,
                mcVanillaBgTopRight,
                mcVanillaBgCenterLeft,
                mcVanillaBgCenter,
                mcVanillaBgCenterRight,
                mcVanillaBgBottomLeft,
                mcVanillaBgBottomCenter,
                mcVanillaBgBottomRight));

        Texture2D sidebarLeftTex = null;
        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/sidebar_left_component.png"));
        if (image != null)
            sidebarLeftTex = RenderUtils.createTexture2D(image);
        ImagePrefab sidebarLeftComponent = new ImagePrefab(sidebarLeftTex);

        Texture2D missingTexture = null;
        image = getBufferedImageFromRl(new ResourceLocation("ometweaks:textures/gui/missing_texture.png"));
        if (image != null)
            missingTexture = RenderUtils.createTexture2D(image);
        GuiResources.missingTexture = new ImagePrefab(new NinePatchBorder(
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture));
        GuiResources.missingTexture.ninePatchBorder.center.tiling = true;
        GuiResources.missingTexture.ninePatchBorder.topLeft.sizeDeductionByPixels = false;
        GuiResources.missingTexture.ninePatchBorder.topRight.sizeDeductionByPixels = false;
        GuiResources.missingTexture.ninePatchBorder.bottomLeft.sizeDeductionByPixels = false;
        GuiResources.missingTexture.ninePatchBorder.bottomRight.sizeDeductionByPixels = false;

        register("vanilla_background", mcVanillaBg);
        register("sidebar_left_component", sidebarLeftComponent);
    }
}
