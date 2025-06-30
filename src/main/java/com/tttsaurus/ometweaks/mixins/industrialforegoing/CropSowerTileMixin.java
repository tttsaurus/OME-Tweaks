package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.agriculture.CropSowerTile;
import com.buuz135.industrial.utils.ItemStackUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
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

    @WrapOperation(
            method = "work",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/common/util/FakePlayer;setHeldItem(Lnet/minecraft/util/EnumHand;Lnet/minecraft/item/ItemStack;)V"
            ))
    public void extraWork(FakePlayer instance, EnumHand enumHand, ItemStack itemStack, Operation<Void> original)
    {
        // todo: custom hoe
        original.call(instance, enumHand, itemStack);
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
        original.call(instance, new ColoredItemHandler(inPlant, EnumDyeColor.GREEN, "Seeds input", new BoundingRectangle(54, 25, 54, 54))
        {
            public boolean canInsertItem(int slot, ItemStack stack)
            {
                boolean canInsert = super.canInsertItem(slot, stack) && (stack.getItem() instanceof IPlantable || ItemStackUtils.isStackOreDict(stack, "treeSapling") || ItemStackUtils.isChorusFlower(stack));
                // todo: custom logic
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
