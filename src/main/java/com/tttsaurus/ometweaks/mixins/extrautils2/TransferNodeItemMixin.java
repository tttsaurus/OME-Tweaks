package com.tttsaurus.ometweaks.mixins.extrautils2;

import com.rwtema.extrautils2.itemhandler.SingleStackHandler;
import com.rwtema.extrautils2.transfernodes.TransferNodeItem;
import com.rwtema.extrautils2.transfernodes.Upgrade;
import com.tttsaurus.fluidintetweaker.common.api.WorldIngredient;
import com.tttsaurus.ometweaks.OMETweaks;
import com.tttsaurus.ometweaks.api.fluidintetweaker.InternalMethods;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.EnumSet;

@Mixin(TransferNodeItem.class)
public class TransferNodeItemMixin
{
    @Shadow(remap = false)
    protected SingleStackHandler stack;

    @Unique
    private static final EnumSet<EnumFacing> OME_Tweaks$surrounding = EnumSet.of(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST);

    @Inject(method = "processBuffer(Lnet/minecraftforge/items/IItemHandler;)V", at = @At("HEAD"), remap = false)
    public void addFluidInteractionTweakerCompat(IItemHandler attached, CallbackInfo ci)
    {
        if (!OMETweaks.IS_FLUIDINTETWEAKER_LOADED) return;
        if (InternalMethods.instance == null) return;

        TransferNodeItem this0 = (TransferNodeItem)(Object)this;

        int upgradeLevel;
        if (attached == null)
        {
            upgradeLevel = this0.getUpgradeLevel(Upgrade.MINING);
            if (upgradeLevel > 0)
            {
                if (stack.isFull()) return;

                World world = this0.holder.getWorld();
                BlockPos pos = this0.holder.getPos().offset(this0.side);
                IBlockState blockState = world.getBlockState(pos);

                WorldIngredient ingredientA = null;
                WorldIngredient ingredientB = null;

                boolean flag = true;
                for (EnumFacing facing : OME_Tweaks$surrounding)
                {
                    WorldIngredient ingredient = WorldIngredient.getFrom(world, pos.add(facing.getDirectionVec()));

                    if (InternalMethods.instance.FluidInteractionRecipeManager$ingredientAExists.invoke(ingredient))
                    {
                        ingredientA = ingredient;
                        for (EnumFacing facing1 : OME_Tweaks$surrounding)
                        {
                            ingredientB = WorldIngredient.getFrom(world, pos.add(facing1.getDirectionVec()));
                            if (InternalMethods.instance.FluidInteractionRecipeManager$recipeExists.invoke(ingredientA, ingredientB))
                            {
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (InternalMethods.instance.FluidInteractionRecipeManager$ingredientBExists.invoke(ingredient))
                    {
                        ingredientB = ingredient;
                        for (EnumFacing facing1 : OME_Tweaks$surrounding)
                        {
                            ingredientA = WorldIngredient.getFrom(world, pos.add(facing1.getDirectionVec()));
                            if (InternalMethods.instance.FluidInteractionRecipeManager$recipeExists.invoke(ingredientA, ingredientB))
                            {
                                flag = false;
                                break;
                            }
                        }
                    }
                }
                if (flag) return;

                if (Minecraft.getMinecraft().player != null)
                    Minecraft.getMinecraft().player.sendChatMessage("debug");
            }
        }
    }
}
