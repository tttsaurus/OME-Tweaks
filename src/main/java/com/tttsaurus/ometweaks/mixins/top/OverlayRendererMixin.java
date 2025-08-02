package com.tttsaurus.ometweaks.mixins.top;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.top.TOPModule;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OverlayRenderer.class)
public class OverlayRendererMixin
{
    @WrapMethod(method = "renderHUD", remap = false)
    private static void renderHUD(ProbeMode mode, float partialTicks, Operation<Void> original)
    {
        if (TOPModule.DISABLE_TOP_HUD_WHEN_GUI_SCREEN_IS_ON && Minecraft.getMinecraft().currentScreen != null) return;

        original.call(mode, partialTicks);
    }
}
