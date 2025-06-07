package com.tttsaurus.ometweaks.mixins.extrautils2;

import com.rwtema.extrautils2.itemhandler.SingleStackHandler;
import com.rwtema.extrautils2.transfernodes.TransferNodeItem;
import com.rwtema.extrautils2.transfernodes.Upgrade;
import com.tttsaurus.fluidintetweaker.common.api.WorldIngredient;
import com.tttsaurus.fluidintetweaker.common.api.WorldIngredientType;
import com.tttsaurus.fluidintetweaker.common.api.event.CustomFluidInteractionEvent;
import com.tttsaurus.fluidintetweaker.common.api.interaction.ComplexOutput;
import com.tttsaurus.fluidintetweaker.common.api.interaction.InteractionEvent;
import com.tttsaurus.fluidintetweaker.common.api.interaction.InteractionEventType;
import com.tttsaurus.fluidintetweaker.common.api.interaction.condition.IEventCondition;
import com.tttsaurus.fluidintetweaker.common.api.util.BlockUtils;
import com.tttsaurus.ometweaks.integration.extrautils2.ExtraUtils2Module;
import com.tttsaurus.ometweaks.integration.fluidintetweaker.FITModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Mixin(TransferNodeItem.class)
public class TransferNodeItemMixin
{
    @Shadow(remap = false)
    protected SingleStackHandler stack;

    @Unique
    private static final EnumSet<EnumFacing> OME_Tweaks$surrounding = EnumSet.of(EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST);

    @Unique
    private BlockPos OME_Tweaks$prevIngredientAPos = null;
    @Unique
    private BlockPos OME_Tweaks$prevIngredientBPos = null;

    @Unique
    private final List<ItemStack> OME_Tweaks$simulatedOutput = new ArrayList<>();

    @Inject(method = "processBuffer(Lnet/minecraftforge/items/IItemHandler;)V", at = @At("HEAD"), remap = false)
    public void addFluidInteractionTweakerCompat(IItemHandler attached, CallbackInfo ci)
    {
        if (!ExtraUtils2Module.ENABLE_XU2_NODE_MINING_FIT_COMPAT) return;
        if (!FITModule.isModLoaded) return;
        if (FITModule.internalMethods == null) return;

        TransferNodeItem this0 = (TransferNodeItem)(Object)this;

        if (attached == null)
        {
            int upgradeLevel = this0.getUpgradeLevel(Upgrade.MINING);
            if (upgradeLevel > 0)
            {
                if (stack.isFull()) return;

                if (!OME_Tweaks$simulatedOutput.isEmpty())
                {
                    stack.insertItem(0, OME_Tweaks$simulatedOutput.get(0), false);
                    OME_Tweaks$simulatedOutput.remove(0);
                    return;
                }

                World world = this0.holder.getWorld();
                BlockPos pos = this0.holder.getPos().offset(this0.side);
                IBlockState blockState = world.getBlockState(pos);

                WorldIngredient ingredientA = null;
                WorldIngredient ingredientB = null;

                boolean flag = true;

                if (OME_Tweaks$prevIngredientAPos != null && OME_Tweaks$prevIngredientBPos != null)
                {
                    ingredientA = WorldIngredient.getFrom(world, OME_Tweaks$prevIngredientAPos);
                    ingredientB = WorldIngredient.getFrom(world, OME_Tweaks$prevIngredientBPos);
                    if (FITModule.internalMethods.FluidInteractionRecipeManager$recipeExists.invoke(ingredientA, ingredientB))
                        flag = false;
                }

                if (flag)
                {
                    for (EnumFacing facing : OME_Tweaks$surrounding)
                    {
                        WorldIngredient ingredient = WorldIngredient.getFrom(world, pos.add(facing.getDirectionVec()));

                        if (FITModule.internalMethods.FluidInteractionRecipeManager$ingredientAExists.invoke(ingredient))
                        {
                            ingredientA = ingredient;
                            for (EnumFacing facing1 : OME_Tweaks$surrounding)
                            {
                                ingredientB = WorldIngredient.getFrom(world, pos.add(facing1.getDirectionVec()));
                                if (FITModule.internalMethods.FluidInteractionRecipeManager$recipeExists.invoke(ingredientA, ingredientB))
                                {
                                    OME_Tweaks$prevIngredientAPos = pos.add(facing.getDirectionVec());
                                    OME_Tweaks$prevIngredientBPos = pos.add(facing1.getDirectionVec());
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if (FITModule.internalMethods.FluidInteractionRecipeManager$ingredientBExists.invoke(ingredient))
                        {
                            ingredientB = ingredient;
                            for (EnumFacing facing1 : OME_Tweaks$surrounding)
                            {
                                ingredientA = WorldIngredient.getFrom(world, pos.add(facing1.getDirectionVec()));
                                if (FITModule.internalMethods.FluidInteractionRecipeManager$recipeExists.invoke(ingredientA, ingredientB))
                                {
                                    OME_Tweaks$prevIngredientBPos = pos.add(facing.getDirectionVec());
                                    OME_Tweaks$prevIngredientAPos = pos.add(facing1.getDirectionVec());
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (flag) return;

                // recipe matches

                ComplexOutput output = FITModule.internalMethods.FluidInteractionRecipeManager$getRecipeOutput.invoke(ingredientA, ingredientB);

                if (output == null)
                {
                    OME_Tweaks$prevIngredientAPos = null;
                    OME_Tweaks$prevIngredientBPos = null;
                    return;
                }

                boolean isSourceA = ingredientA.getIsFluidSource();
                boolean consumesA = isSourceA || ingredientA.getIngredientType() == WorldIngredientType.BLOCK;
                if (consumesA && ingredientA.getIngredientType() == WorldIngredientType.FLUID)
                {
                    ingredientA.setIsFluidSource(false);
                    if (FITModule.internalMethods.FluidInteractionRecipeManager$recipeExists.invoke(ingredientA, ingredientB))
                        consumesA = false;
                    ingredientA.setIsFluidSource(isSourceA);
                }

                if (consumesA) world.setBlockToAir(OME_Tweaks$prevIngredientAPos);

                CustomFluidInteractionEvent fluidInteractionEvent = new CustomFluidInteractionEvent(
                        world,
                        pos,
                        false,
                        false,
                        blockState,
                        ingredientA,
                        ingredientB,
                        output);

                for (InteractionEvent event: output.getEvents())
                {
                    boolean simulate = false;

                    if (event.getEventType() == InteractionEventType.SetBlock)
                        simulate = true;
                    else if (event.getEventType() == InteractionEventType.SpawnEntityItem)
                        simulate = true;

                    if (simulate)
                    {
                        boolean pass = true;
                        List<IEventCondition> conditions = event.getConditions();
                        for (IEventCondition condition: conditions)
                        {
                            if (!condition.judge(fluidInteractionEvent))
                            {
                                pass = false;
                                break;
                            }
                        }
                        if (!pass) continue;

                        if (event.getEventType() == InteractionEventType.SetBlock)
                            OME_Tweaks$simulatedOutput.add(BlockUtils.getItemStack(event.getBlockState()));
                        else if (event.getEventType() == InteractionEventType.SpawnEntityItem)
                            OME_Tweaks$simulatedOutput.add(event.getItemStack().copy());
                    }
                }
            }
        }
    }
}
