package com.tttsaurus.ometweaks.mixins.early;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration._omefactory.OMEFactoryModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockCake.class)
public class BlockCakeMixin
{
    @WrapMethod(method = "neighborChanged")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, Operation<Void> original)
    {
        if (OMEFactoryModule.DISABLE_CAKE_AUTO_DESTROY) return;

        original.call(state, worldIn, pos, blockIn, fromPos);
    }
}
