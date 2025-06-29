package com.tttsaurus.ometweaks.integration.industrialforegoing.animalrancher;

import com.buuz135.industrial.proxy.BlockRegistry;
import com.tttsaurus.ometweaks.render.EntityRenderer;
import com.tttsaurus.ometweaks.render.RenderUtils;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import org.apache.commons.lang3.time.StopWatch;
import java.awt.*;

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
        RenderUtils.storeCommonGlStates();

        RenderHelper.enableGUIStandardItemLighting();
        minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, animalRancher, JEI_PIVOT_X - 4, JEI_PIVOT_Y - 4);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.translate(JEI_PIVOT_X + 13, JEI_PIVOT_Y + 8, 0);
        GlStateManager.scale(1, 0.55f, 1);
        GlStateManager.rotate(45, 0, 0, 1);
        RenderUtils.renderRoundedRect(0, 0, 12, 12, 3, (new Color(144, 144, 144, 144)).getRGB());
        GlStateManager.popMatrix();

        RenderUtils.restoreCommonGlStates();

        if (entityRenderer == null)
            entityRenderer = new EntityRenderer(minecraft, inputEntity);

        float dist = 90f;
        double angle = Math.atan((mouseX - (JEI_PIVOT_X + 12))/dist) / (2f * 3.14159f) * 360f;

        if (output.damage == 0)
        {
            entityRenderer.render(
                    JEI_PIVOT_X + 13 + output.modelPosX,
                    JEI_PIVOT_Y + 14 + output.modelPosY,
                    output.modelScaleX,
                    output.modelScaleY,
                    output.modelScaleZ,
                    output.modelRotateX,
                    (float)angle + output.modelRotateY,
                    output.modelRotateZ,
                    Color.WHITE.getRGB());
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

            entityRenderer.render(
                    JEI_PIVOT_X + 13 + output.modelPosX,
                    JEI_PIVOT_Y + 14 + output.modelPosY,
                    output.modelScaleX,
                    output.modelScaleY,
                    output.modelScaleZ,
                    output.modelRotateX,
                    (float)angle + output.modelRotateY,
                    output.modelRotateZ,
                    color);

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

        RenderUtils.restoreCommonGlStates();
    }
}
