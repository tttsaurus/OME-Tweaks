package com.tttsaurus.ometweaks.mixins.thermalfoundation;

import cofh.thermalfoundation.fluid.BlockFluidPetrotheum;
import com.tttsaurus.ometweaks.integration.thermalfoundation.ThermalFoundationModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockFluidPetrotheum.class)
public class BlockFluidPetrotheumMixin
{
    @Inject(method = "addInteractions", at = @At("HEAD"), remap = false, cancellable = true)
    public void addInteractions(CallbackInfo ci)
    {
        if (ThermalFoundationModule.DISABLE_TF_PETROTHEUM_INTERACTIONS)
            ci.cancel();
    }
}
