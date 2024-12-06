package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.item.infinity.ItemInfinityDrill;
import com.buuz135.industrial.item.infinity.ItemInfinityDrill.DrillTier;
import com.buuz135.industrial.utils.RayTraceUtils;
import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.util.Map;

@SuppressWarnings("all")
@Mixin(ItemInfinityDrill.class)
public abstract class ItemInfinityDrillMixin
{
    @Shadow
    public abstract DrillTier getSelectedDrillTier(ItemStack stack);

    @Shadow
    public abstract Pair<BlockPos, BlockPos> getArea(BlockPos pos, EnumFacing facing, DrillTier currentTier, boolean withDepth);

    @Shadow
    protected abstract boolean enoughFuel(ItemStack stack);

    @Shadow
    protected abstract void consumeFuel(ItemStack stack);

    /**
     * @author tttsaurus
     * @reason To add a custom blacklist and harvest level check to infinity drill's harvest logic, and this method is modified from com.buuz135.industrial.item.infinity.ItemInfinityDrill.onBlockDestroyed
     */
    @Overwrite
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            RayTraceResult rayTraceResult = RayTraceUtils.rayTraceSimple(worldIn, entityLiving, 16, 0);
            EnumFacing facing = rayTraceResult.sideHit;
            DrillTier currentTier = getSelectedDrillTier(stack);
            Pair<BlockPos, BlockPos> area = getArea(pos, facing, currentTier, true);
            BlockPos.getAllInBox(area.getLeft(), area.getRight()).forEach(blockPos ->
            {
                if (enoughFuel(stack) && worldIn.getTileEntity(blockPos) == null && entityLiving instanceof EntityPlayerMP && !worldIn.isAirBlock(blockPos))
                {
                    IBlockState tempState = worldIn.getBlockState(blockPos);
                    Block block = tempState.getBlock();
                    if (block == Blocks.AIR) return;
                    if (OMEConfig.ENABLE)
                    {
                        if (OMEConfig.ENABLE_IF_INFINITY_DRILL_BLACKLIST)
                        {
                            ItemStack itemStack = new ItemStack(block, 1, block.getMetaFromState(tempState));
                            for (ItemStack blacklistItemStack : OMEConfig.IF_INFINITY_DRILL_BLACKLIST)
                                if (itemStack.isItemEqual(blacklistItemStack)) return;
                        }
                        if (OMEConfig.ENABLE_IF_INFINITY_DRILL_HARVEST_LEVEL)
                        {
                            boolean flag = true;
                            int targetHarvestLevel = block.getHarvestLevel(tempState);
                            for (Map.Entry<String, Integer> entry : OMEConfig.IF_INFINITY_DRILL_HARVEST_LEVEL.entrySet())
                                if (block.isToolEffective(entry.getKey(), tempState) && entry.getValue() >= targetHarvestLevel)
                                    flag = false;
                            if (flag) return;
                        }
                    }
                    if (block.getBlockHardness(tempState, worldIn, blockPos) < 0) return;

                    int xp = ForgeHooks.onBlockBreakEvent(worldIn, ((EntityPlayerMP) entityLiving).interactionManager.getGameType(), (EntityPlayerMP) entityLiving, blockPos);
                    if (xp >= 0 && block.removedByPlayer(tempState, worldIn, blockPos, (EntityPlayer) entityLiving, true))
                    {
                        block.onPlayerDestroy(worldIn, blockPos, tempState);
                        block.harvestBlock(worldIn, (EntityPlayer) entityLiving, blockPos, tempState, null, stack);
                        block.dropXpOnBlockBreak(worldIn, blockPos, xp);
                        consumeFuel(stack);
                    }
                }
            });
            worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(area.getLeft(), area.getRight()).grow(1)).forEach(entityItem ->
            {
                entityItem.setPositionAndUpdate(entityLiving.posX, entityLiving.posY, entityLiving.posZ);
                entityItem.setPickupDelay(0);
            });
            worldIn.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(area.getLeft(), area.getRight()).grow(1)).forEach(entityXPOrb -> entityXPOrb.setPositionAndUpdate(entityLiving.posX, entityLiving.posY, entityLiving.posZ));
        }

        return false;
    }
}
