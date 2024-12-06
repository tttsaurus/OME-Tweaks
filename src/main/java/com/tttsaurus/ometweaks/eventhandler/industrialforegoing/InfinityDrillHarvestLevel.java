package com.tttsaurus.ometweaks.eventhandler.industrialforegoing;

import com.tttsaurus.ometweaks.OMEConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class InfinityDrillHarvestLevel
{
    @SubscribeEvent()
    public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event)
    {
        EntityPlayer player = event.getHarvester();
        World world = player.world;
        if (world.isRemote) return;

        ResourceLocation rl = player.getHeldItemMainhand().getItem().getRegistryName();
        if (rl == null) return;
        if (!rl.toString().equals("industrialforegoing:infinity_drill")) return;

        IBlockState blockState = event.getState();
        Block block = blockState.getBlock();

        boolean canHarvest = blockState.getMaterial().isToolNotRequired();

        String requiredToolClass = block.getHarvestTool(blockState);
        if (requiredToolClass == null)
            canHarvest = true;
        else if (OMEConfig.IF_INFINITY_DRILL_HARVEST_LEVEL.containsKey(requiredToolClass))
        {
            int toolLevel = OMEConfig.IF_INFINITY_DRILL_HARVEST_LEVEL.get(requiredToolClass);
            if (toolLevel >= block.getHarvestLevel(blockState)) canHarvest = true;
        }

        if (!canHarvest) event.getDrops().clear();
    }
}
