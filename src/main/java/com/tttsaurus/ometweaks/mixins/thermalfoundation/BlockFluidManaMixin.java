package com.tttsaurus.ometweaks.mixins.thermalfoundation;

import cofh.thermalfoundation.fluid.BlockFluidMana;
import com.tttsaurus.ometweaks.OMEConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockFluidMana.class)
public class BlockFluidManaMixin
{
    @Inject(method = "addInteractions", at = @At("HEAD"), remap = false, cancellable = true)
    public void addInteractions(CallbackInfo ci)
    {
        if (OMEConfig.DISABLE_TF_MANA_INTERACTIONS)
            ci.cancel();
    }
}
