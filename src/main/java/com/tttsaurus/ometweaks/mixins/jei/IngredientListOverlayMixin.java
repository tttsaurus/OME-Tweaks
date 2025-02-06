package com.tttsaurus.ometweaks.mixins.jei;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ometweaks.OMEConfig;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.overlay.IngredientListOverlay;
import mezz.jei.input.GuiTextFieldFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.awt.*;
import java.util.Set;

@Mixin(IngredientListOverlay.class)
public class IngredientListOverlayMixin
{
    @WrapOperation(
            method = "updateScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/input/GuiTextFieldFilter;updateBounds(Ljava/awt/Rectangle;)V"
            ),
            remap = false)
    public void updateScreenMixin1(GuiTextFieldFilter instance, Rectangle area, Operation<Void> original)
    {
        if (OMEConfig.DISABLE_JEI_CONFIG_BUTTON)
            original.call(instance, new Rectangle(area.x, area.y, area.width + 20, area.height));
        else
            original.call(instance, area);
    }

    @WrapOperation(
            method = "updateScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/gui/overlay/IngredientGridWithNavigation;updateBounds(Ljava/awt/Rectangle;Ljava/util/Set;I)Z"
            ),
            remap = false)
    public boolean updateScreenMixin2(IngredientGridWithNavigation instance, Rectangle availableArea, Set<Rectangle> guiExclusionAreas, int minWidth, Operation<Boolean> original)
    {
        if (OMEConfig.DISABLE_JEI_CONFIG_BUTTON)
            return original.call(instance, availableArea, guiExclusionAreas, 0);
        else
            return original.call(instance, availableArea, guiExclusionAreas, minWidth);
    }

    @WrapOperation(
            method = "updateScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/gui/elements/GuiIconToggleButton;updateBounds(Ljava/awt/Rectangle;)V"
            ),
            remap = false)
    public void updateScreenMixin3(GuiIconToggleButton instance, Rectangle area, Operation<Void> original)
    {
        if (!OMEConfig.DISABLE_JEI_CONFIG_BUTTON)
            original.call(instance, area);
    }
}
