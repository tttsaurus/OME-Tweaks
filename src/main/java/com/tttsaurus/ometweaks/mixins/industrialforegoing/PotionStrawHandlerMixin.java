package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.proxy.FluidsRegistry;
import com.buuz135.industrial.utils.apihandlers.straw.PotionStrawHandler;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionStrawHandler.class)
public class PotionStrawHandlerMixin
{
    @Unique
    private String OME_Tweaks$fluidName;

    @Inject(method = "<init>(Lnet/minecraftforge/fluids/Fluid;)V", at = @At("RETURN"), remap = false)
    public void onConstruct1(Fluid fluid, CallbackInfo ci)
    {
        OME_Tweaks$fluidName = fluid.getName();
    }

    @Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"), remap = false)
    public void onConstruct2(String fluidName, CallbackInfo ci)
    {
        OME_Tweaks$fluidName = fluidName;
    }

    @WrapMethod(method = "addPotion(Lnet/minecraft/potion/Potion;Ljava/lang/Integer;Ljava/lang/Integer;)Lcom/buuz135/industrial/utils/apihandlers/straw/PotionStrawHandler;", remap = false)
    public PotionStrawHandler addPotion(Potion potion, Integer duration, Integer amplifier, Operation<PotionStrawHandler> original)
    {
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_DRINKING_BIOFUEL && OME_Tweaks$fluidName.equals(FluidsRegistry.BIOFUEL.getName()))
            return (PotionStrawHandler)(Object)this;
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_DRINKING_SLUDGE && OME_Tweaks$fluidName.equals(FluidsRegistry.SLUDGE.getName()))
            return (PotionStrawHandler)(Object)this;
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_DRINKING_SEWAGE && OME_Tweaks$fluidName.equals(FluidsRegistry.SEWAGE.getName()))
            return (PotionStrawHandler)(Object)this;
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_DRINKING_MEAT && OME_Tweaks$fluidName.equals(FluidsRegistry.MEAT.getName()))
            return (PotionStrawHandler)(Object)this;
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_DRINKING_PROTEIN && OME_Tweaks$fluidName.equals(FluidsRegistry.PROTEIN.getName()))
            return (PotionStrawHandler)(Object)this;
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_DRINKING_LATEX && OME_Tweaks$fluidName.equals(FluidsRegistry.LATEX.getName()))
            return (PotionStrawHandler)(Object)this;
        return original.call(potion, duration, amplifier);
    }
}
