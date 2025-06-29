package com.tttsaurus.ometweaks.mixins.industrialforegoing;

import com.buuz135.industrial.tile.generator.AbstractFuelGenerator;
import com.buuz135.industrial.utils.WorkUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.tttsaurus.ometweaks.integration.industrialforegoing.machine.generator.petrified.FuelDef;
import com.tttsaurus.ometweaks.integration.industrialforegoing.IndustrialForegoingModule;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
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
    @WrapMethod(method = "consumeFuel", remap = false)
    public long consumeFuel(Operation<Long> original)
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

                if (IndustrialForegoingModule.ENABLE_IF_PETRIFIED_FUEL_GENERATOR)
                {
                    boolean specified = false;
                    for (Map.Entry<ItemStack, FuelDef> entry: IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_FUELS.entrySet())
                        if (this0.getCurrent().isItemEqual(entry.getKey()))
                        {
                            burnTime = entry.getValue().duration;
                            specified = true;
                            break;
                        }
                    if (!specified && IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX != -1)
                        burnTime = burnTime > IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX ?
                                IndustrialForegoingModule.IF_PETRIFIED_FUEL_GENERATOR_BURN_TIME_MAX : burnTime;
                }

                temp.setCount(temp.getCount() - 1);
                return (long)((float)(burnTime * 100) * this0.getMultiplier() / 0.6f);
            }
        }
    }
}
