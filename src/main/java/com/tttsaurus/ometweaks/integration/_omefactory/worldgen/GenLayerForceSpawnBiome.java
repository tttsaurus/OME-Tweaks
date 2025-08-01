package com.tttsaurus.ometweaks.integration._omefactory.worldgen;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.IntCache;
import javax.annotation.Nonnull;

public final class GenLayerForceSpawnBiome extends GenLayerRiverMix
{
    private final int biomeIdInner;
    private final int radiusInner;
    private final int biomeIdOuter;
    private final int radiusOuter;

    public GenLayerForceSpawnBiome(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_, int biomeIdInner, int radiusInner, int biomeIdOuter, int radiusOuter)
    {
        super(p_i2129_1_, p_i2129_3_, p_i2129_4_);
        this.biomeIdInner = biomeIdInner;
        this.radiusInner = radiusInner;
        this.biomeIdOuter = biomeIdOuter;
        this.radiusOuter = radiusOuter;
    }

    @Override
    public @Nonnull int[] getInts(int areaX, int areaZ, int width, int height)
    {
        int[] originalBiomes = super.getInts(areaX, areaZ, width, height);

        float roughNorm = (float)Math.sqrt(areaX * areaX + areaZ * areaZ);
        if (roughNorm > radiusOuter) return originalBiomes;

        int[] output = IntCache.getIntCache(width * height);

        int radiusInner = this.radiusInner >> 2;
        int radiusOuter = this.radiusOuter >> 2;

        for (int dz = 0; dz < height; ++dz)
        {
            for (int dx = 0; dx < width; ++dx)
            {
                int x = areaX + dx;
                int z = areaZ + dz;
                float norm = (float)Math.sqrt(x * x + z * z);

                if (norm <= radiusInner)
                    output[dx + dz * width] = this.biomeIdInner;
                else if (norm <= radiusOuter)
                    output[dx + dz * width] = this.biomeIdOuter;
                else
                    output[dx + dz * width] = originalBiomes[dx + dz * width];
            }
        }

        return output;
    }
}
