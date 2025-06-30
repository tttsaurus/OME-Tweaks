package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.tttsaurus.ometweaks.integration.jei.IJEIExclusionAreaGuiPiece;
import net.ndrei.teslacorelib.gui.SideDrawerPiece;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SideDrawerPiece.class)
public class SideDrawerPieceMixin implements IJEIExclusionAreaGuiPiece
{
    @Final
    @Shadow(remap = false)
    private int topIndex;

    @Override
    public int exclusionAreaX()
    {
        return -12;
    }

    @Override
    public int exclusionAreaY()
    {
        return 5 + topIndex * 14;
    }

    @Override
    public int exclusionAreaWidth()
    {
        return 14;
    }

    @Override
    public int exclusionAreaHeight()
    {
        return 14;
    }
}
