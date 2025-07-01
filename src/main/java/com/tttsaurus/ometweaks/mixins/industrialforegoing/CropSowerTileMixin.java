package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.agriculture.CropSowerTile;
import com.buuz135.industrial.utils.ItemStackUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.IMachineWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandler;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.gui.TiledRenderedGuiPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.inventory.ColoredItemHandler;
import net.ndrei.teslacorelib.inventory.LockableItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(CropSowerTile.class)
public class CropSowerTileMixin
{
    @Shadow(remap = false)
    private boolean hoeGround;

    @Unique
    private BlockPos OME_Tweaks$currPos;

    @WrapOperation(
            method = "work",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;get(I)Ljava/lang/Object;"
            ),
            remap = false)
    public Object getPos(List instance, int i, Operation<Object> original)
    {
        OME_Tweaks$currPos = (BlockPos)original.call(instance, i);
        return OME_Tweaks$currPos;
    }

    @Unique
    private boolean OME_Tweaks$hasWorked = false;

    @Inject(method = "work", at = @At("TAIL"), cancellable = true, remap = false)
    public void afterWork(CallbackInfoReturnable<Float> cir)
    {
        if (OME_Tweaks$hasWorked)
        {
            OME_Tweaks$hasWorked = false;
            cir.setReturnValue(1f);
        }
    }

    @WrapOperation(
            method = "work",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/FakePlayer;setHeldItem(Lnet/minecraft/util/EnumHand;Lnet/minecraft/item/ItemStack;)V"
            ))
    public void extraWork(FakePlayer instance, EnumHand enumHand, ItemStack itemStack, Operation<Void> original)
    {
        if (!IndustrialForegoingModule.ENABLE_IF_PLANT_SOWER_MOD)
        {
            original.call(instance, enumHand, itemStack);
            return;
        }

        IMachineWorldProvider worldProvider = (IMachineWorldProvider)(CropSowerTile)(Object)this;
        World world = worldProvider.getWorld();

        if (hoeGround)
        {
            if (IndustrialForegoingModule.ENABLE_IF_PLANT_SOWER_TEASTORY_COMPAT)
            {
                // teastory compat
                if (itemStack.getItem().getRegistryName() != null && itemStack.getItem().getRegistryName().toString().equals("teastory:item_xian_rice_seedling"))
                {
                    IBlockState blockState = world.getBlockState(OME_Tweaks$currPos.offset(EnumFacing.DOWN));
                    if (blockState.getBlock().equals(Blocks.FARMLAND))
                    {
                        IBlockState field = null;
                        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("teastory:field"));
                        if (block != null) field = block.getDefaultState();

                        if (field != null)
                        {
                            world.setBlockState(OME_Tweaks$currPos.offset(EnumFacing.DOWN), field);
                            instance.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
                            block.onBlockActivated(world, OME_Tweaks$currPos.offset(EnumFacing.DOWN), field, instance, EnumHand.MAIN_HAND, EnumFacing.DOWN, 0, 0, 0);
                        }
                    }
                }
            }
        }

        original.call(instance, enumHand, itemStack);

        if (IndustrialForegoingModule.ENABLE_IF_PLANT_SOWER_TEASTORY_COMPAT)
        {
            // teastory compat
            if (itemStack.getItem().getRegistryName() != null && itemStack.getItem().getRegistryName().toString().equals("teastory:item_xian_rice_seedling"))
            {
                instance.setPositionAndRotation(OME_Tweaks$currPos.getX(), OME_Tweaks$currPos.getY(), OME_Tweaks$currPos.getZ(), 90.0F, 90.0F);
                itemStack.useItemRightClick(world, instance, EnumHand.MAIN_HAND);
                OME_Tweaks$hasWorked = true;
            }
        }
    }

    @Shadow(remap = false)
    private LockableItemHandler inPlant;

    @WrapOperation(
            method = "initializeInventories",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/buuz135/industrial/tile/agriculture/CropSowerTile;addInventory(Lnet/minecraftforge/items/IItemHandler;)V"
            ),
            remap = false)
    public void addInventory(CropSowerTile instance, IItemHandler iItemHandler, Operation<Void> original)
    {
        if (!IndustrialForegoingModule.ENABLE_IF_PLANT_SOWER_MOD)
        {
            original.call(instance, iItemHandler);
            return;
        }

        original.call(instance, new ColoredItemHandler(inPlant, EnumDyeColor.GREEN, "Seeds input", new BoundingRectangle(54, 25, 54, 54))
        {
            public boolean canInsertItem(int slot, ItemStack stack)
            {
                boolean canInsert = super.canInsertItem(slot, stack) && (stack.getItem() instanceof IPlantable || ItemStackUtils.isStackOreDict(stack, "treeSapling") || ItemStackUtils.isChorusFlower(stack));

                if (IndustrialForegoingModule.ENABLE_IF_PLANT_SOWER_EXTRA_ACCEPTABLE_CROPS)
                {
                    for (ItemStack itemStack: IndustrialForegoingModule.IF_PLANT_SOWER_EXTRA_ACCEPTABLE_CROPS)
                        if (stack.isItemEqual(itemStack))
                            canInsert = true;
                }

                return canInsert;
            }

            public boolean canExtractItem(int slot)
            {
                return false;
            }

            public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer<?> container)
            {
                List<IGuiContainerPiece> guiContainerPieces = super.getGuiContainerPieces(container);
                int i = 0;

                for(int x = 0; x < 3; ++x)
                {
                    for(int y = 0; y < 3; ++y)
                    {
                        guiContainerPieces.add(new TiledRenderedGuiPiece(54 + 18 * x, 25 + 18 * y, 18, 18, 1, 1, BasicTeslaGuiContainer.Companion.getMACHINE_BACKGROUND(), 108, 225, CropSowerTile.colors[i]));
                        ++i;
                    }
                }

                return guiContainerPieces;
            }
        });
    }
}
