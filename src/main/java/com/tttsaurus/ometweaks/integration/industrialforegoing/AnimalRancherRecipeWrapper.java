package com.tttsaurus.ometweaks.integration.industrialforegoing;

import com.buuz135.industrial.proxy.BlockRegistry;
import com.tttsaurus.ometweaks.utils.EntityRenderer;
import com.tttsaurus.ometweaks.utils.RenderUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import org.apache.commons.lang3.time.StopWatch;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class AnimalRancherRecipeWrapper implements IRecipeWrapper
{
    public final EntityEntry inputEntity;
    public final AnimalRancherOutput output;

    private final ItemStack animalRancher;

    public AnimalRancherRecipeWrapper(EntityEntry inputEntity, AnimalRancherOutput output)
    {
        this.inputEntity = inputEntity;
        this.output = output;
        animalRancher = new ItemStack(BlockRegistry.animalResourceHarvesterBlock);
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        if (output.itemStack != null)
            ingredients.setOutput(VanillaTypes.ITEM, output.itemStack);
        if (output.fluidStack != null)
            ingredients.setOutput(VanillaTypes.FLUID, output.fluidStack);
    }

    private static final int JEI_PIVOT_X = 24;
    private static final int JEI_PIVOT_Y = 18;

    private EntityRenderer entityRenderer = null;
    private StopWatch stopWatch = null;

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        storeCommonGlStates();

        RenderHelper.enableGUIStandardItemLighting();
        minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, animalRancher, JEI_PIVOT_X - 4, JEI_PIVOT_Y - 4);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.translate(JEI_PIVOT_X + 13, JEI_PIVOT_Y + 8, 0);
        GlStateManager.scale(1, 0.55f, 1);
        GlStateManager.rotate(45, 0, 0, 1);
        RenderUtils.renderRoundedRect(0, 0, 12, 12, 3, (new Color(144, 144, 144, 144)).getRGB());
        GlStateManager.popMatrix();

        restoreCommonGlStates();

        if (entityRenderer == null)
            entityRenderer = new EntityRenderer(minecraft, inputEntity);

        float dist = 90f;
        double angle = Math.atan((mouseX - (JEI_PIVOT_X + 12))/dist) / (2f * 3.14159f) * 360f;

        if (output.damage == 0)
        {
            entityRenderer.render(JEI_PIVOT_X + 13, JEI_PIVOT_Y + 14, 0, (float)angle, 0, Color.WHITE.getRGB());
        }
        else
        {
            if (stopWatch == null)
                stopWatch = new StopWatch();
            if (!stopWatch.isStarted())
                stopWatch.start();

            int color = Color.WHITE.getRGB();
            double time = stopWatch.getNanoTime() / 1e9d;

            if (time >= 1d)
                color = (new Color(200, 50, 0, 255)).getRGB();

            if (time > 1.5d)
            {
                stopWatch.stop();
                stopWatch.reset();
                stopWatch.start();
            }

            entityRenderer.render(JEI_PIVOT_X + 13, JEI_PIVOT_Y + 14, 0, (float)angle, 0, color);

            if (time >= 1d && time <= 1.5d)
            {
                // domain: [0.7, 3.3]
                double x = ((stopWatch.getNanoTime() / 1e9d) - 1d) / 0.5d * 2.6d + 0.7d;
                int a = 255 - (int)(((stopWatch.getNanoTime() / 1e9d) - 1d) * 2 * 255) + 30;
                a = Math.min(a, 255);
                a = Math.max(a, 0);
                RenderUtils.renderText("-" + output.damage, JEI_PIVOT_X + 13, JEI_PIVOT_Y + 14 - (float)(Math.sin(x) * 5f), 1, (new Color(255, 0, 0, a)).getRGB(), true);
            }
        }

        restoreCommonGlStates();
    }

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
    private static final IntBuffer INT_BUFFER_16 = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
    private static final FloatBuffer FLOAT_BUFFER_16 = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();

    private static void storeCommonGlStates()
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
    private static void restoreCommonGlStates()
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
}
