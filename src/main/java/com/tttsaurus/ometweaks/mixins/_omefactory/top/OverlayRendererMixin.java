package com.tttsaurus.ometweaks.mixins._omefactory.top;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration._omefactory.OMEFactoryModule;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OverlayRenderer.class)
public class OverlayRendererMixin
{
    @WrapMethod(method = "renderHUD", remap = false)
    private static void renderHUD(ProbeMode mode, float partialTicks, Operation<Void> original)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null)
        {
            float norm = (float)Math.sqrt(player.posX * player.posX + player.posZ * player.posZ);
            if (norm >= OMEFactoryModule.PLAYER_SAFE_ZONE_RADIUS) return;
        }

        original.call(mode, partialTicks);
    }
}
