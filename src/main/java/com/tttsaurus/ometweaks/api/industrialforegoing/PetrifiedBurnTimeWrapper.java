package com.tttsaurus.ometweaks.api.industrialforegoing;

import com.buuz135.industrial.tile.generator.PetrifiedFuelGeneratorTile;
import com.tttsaurus.ometweaks.OMEConfig;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import java.awt.*;

@SuppressWarnings("all")
public class PetrifiedBurnTimeWrapper implements IRecipeWrapper
{
    public ItemStack itemStack;
    public int duration;
    public long rate;

    public PetrifiedBurnTimeWrapper(ItemStack itemStack, int duration)
    {
        this.itemStack = itemStack;
        this.duration = duration;
        this.rate = PetrifiedFuelGeneratorTile.getEnergy(duration);
    }
    public PetrifiedBurnTimeWrapper(ItemStack itemStack, int duration, int rate)
    {
        this.itemStack = itemStack;
        this.duration = duration;
        this.rate = rate;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInput(ItemStack.class, this.itemStack);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        FontRenderer fontRenderer = minecraft.fontRenderer;

        if (OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_JEI_OVERHAUL)
        {
            fontRenderer.drawString("Power: " + rate + " RF/tick", 24, 8, Color.gray.getRGB());
            fontRenderer.drawString("Burn Time: " + duration + " RF/tick", 24, 20, Color.gray.getRGB());
        }
        else
        {
            fontRenderer.drawString("Power: " + rate + " RF/tick", 24, 8, Color.gray.getRGB());
        }
    }
}
