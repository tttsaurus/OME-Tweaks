package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.fluid.IFCustomFluid;
import com.buuz135.industrial.fluid.IFCustomFluidBlock;
import com.buuz135.industrial.proxy.FluidsRegistry;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.Consumer;

@Mixin(IFCustomFluidBlock.class)
public class IFCustomFluidBlockMixin
{
    @Shadow(remap = false)
    private Consumer<EntityLivingBase> consumer;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    public void onConstruct(IFCustomFluid fluid, Material material, Consumer<EntityLivingBase> consumer1, CallbackInfo ci)
    {
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_BIOFUEL && fluid.getName().equals(FluidsRegistry.BIOFUEL.getName()))
            consumer = (e) -> {};
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_SLUDGE && fluid.getName().equals(FluidsRegistry.SLUDGE.getName()))
            consumer = (e) -> {};
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_SEWAGE && fluid.getName().equals(FluidsRegistry.SEWAGE.getName()))
            consumer = (e) -> {};
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_MEAT && fluid.getName().equals(FluidsRegistry.MEAT.getName()))
            consumer = (e) -> {};
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_PROTEIN && fluid.getName().equals(FluidsRegistry.PROTEIN.getName()))
            consumer = (e) -> {};
        if (IndustrialForegoingModule.DISABLE_IF_FLUID_EFFECT_LATEX && fluid.getName().equals(FluidsRegistry.LATEX.getName()))
            consumer = (e) -> {};
    }
}
