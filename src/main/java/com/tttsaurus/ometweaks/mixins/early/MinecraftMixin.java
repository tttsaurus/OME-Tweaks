package com.tttsaurus.ometweaks.mixins.early;

import com.tttsaurus.ometweaks.OMETweaks;
import com.tttsaurus.ometweaks.render.GlResourceManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(method = "shutdownMinecraftApplet", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;unloadSounds()V", shift = At.Shift.AFTER))
    public void shutdown(CallbackInfo ci)
    {
        OMETweaks.LOGGER.info("Start disposing OpenGL resources");
        GlResourceManager.disposeAll(OMETweaks.LOGGER);
        OMETweaks.LOGGER.info("OpenGL resources disposed");
    }
}
