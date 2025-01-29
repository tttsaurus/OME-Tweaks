package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.generator.AbstractFuelGenerator;
import com.buuz135.industrial.utils.WorkUtils;
import com.tttsaurus.ometweaks.OMEConfig;
import com.tttsaurus.ometweaks.api.industrialforegoing.FuelDef;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.util.Map;

@Mixin(AbstractFuelGenerator.class)
public class AbstractFuelGeneratorMixin
{
    @Shadow(remap = false)
    private int burnTime;

    /**
     * @author tttsaurus
     * @reason To support custom fuels, and this method is modified from com.buuz135.industrial.tile.generator.AbstractFuelGenerator.consumeFuel
     */
    @Overwrite(remap = false)
    public long consumeFuel()
    {
        AbstractFuelGenerator this0 = (AbstractFuelGenerator)(Object)this;

        if (WorkUtils.isDisabled(this0.getBlockType()))
            return 0l;
        else
        {
            ItemStack temp = this0.getFirstFuel(true);
            if (temp.isEmpty())
                return 0l;
            else
            {
                burnTime = TileEntityFurnace.getItemBurnTime(temp);

                if (OMEConfig.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
                {
                    for (Map.Entry<ItemStack, FuelDef> entry: OMEConfig.IF_PETRIFIED_FUEL_GENERATOR_FUELS.entrySet())
                        if (this0.getCurrent().isItemEqual(entry.getKey()))
                        {
                            burnTime = entry.getValue().duration;
                            break;
                        }
                }

                temp.setCount(temp.getCount() - 1);
                return (long)((float)(burnTime * 100) * this0.getMultiplier());
            }
        }
    }
}
