package com.tttsaurus.ometweaks.mixins.thermalfoundation;

import cofh.thermalfoundation.fluid.BlockFluidPyrotheum;
import com.tttsaurus.ometweaks.OMEConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockFluidPyrotheum.class)
public class BlockFluidPyrotheumMixin
{
    @Inject(method = "addInteractions", at = @At("HEAD"), remap = false, cancellable = true)
    public void addInteractions(CallbackInfo ci)
    {
        if (OMEConfig.DISABLE_TF_PYROTHEUM_INTERACTIONS)
            ci.cancel();
    }
}
