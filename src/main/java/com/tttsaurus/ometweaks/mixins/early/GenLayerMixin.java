package com.tttsaurus.ometweaks.mixins.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ometweaks.integration._omefactory.OMEFactoryModule;
import com.tttsaurus.ometweaks.integration._omefactory.worldgen.GenLayerForceSpawnBiome;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GenLayer.class)
public class GenLayerMixin
{
    @WrapOperation(
            method = "initializeAllBiomeGenerators",
            at = @At(
                    value = "NEW",
                    target = "(JLnet/minecraft/world/gen/layer/GenLayer;Lnet/minecraft/world/gen/layer/GenLayer;)Lnet/minecraft/world/gen/layer/GenLayerRiverMix;"
            ))
    private static GenLayerRiverMix newGenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_, Operation<GenLayerRiverMix> original)
    {
        if (OMEFactoryModule.ENABLE_OMEFACTORY_MODULE && OMEFactoryModule.ENABLE_FORCE_SPAWN_BIOME)
        {
            Biome innerBiome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(OMEFactoryModule.INNER_BIOME_NAME));
            Biome outerBiome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(OMEFactoryModule.OUTER_BIOME_NAME));

            if (innerBiome != null && outerBiome != null)
            {
                return new GenLayerForceSpawnBiome(
                        p_i2129_1_,
                        p_i2129_3_,
                        p_i2129_4_,
                        Biome.getIdForBiome(innerBiome),
                        OMEFactoryModule.INNER_BIOME_RADIUS,
                        Biome.getIdForBiome(outerBiome),
                        OMEFactoryModule.OUTER_BIOME_RADIUS);
            }
        }

        return original.call(p_i2129_1_, p_i2129_3_, p_i2129_4_);
    }
}
