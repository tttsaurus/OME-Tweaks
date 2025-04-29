package com.tttsaurus.ometweaks.misc.enderio;

import net.minecraftforge.fml.common.event.FMLInterModComms;

public final class EnderIOUtils
{
    public static void sendGrindingBallIMC(String oreDict, float grinding, float chance, float power, int durability)
    {
        FMLInterModComms.sendMessage("enderio", "recipe:xml",
                String.format("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <recipes>
                            <grindingball name="%s" grinding="%f" chance="%f" power="%f" durability="%d" >
                                <item name="oredict:%s"/>
                            </grindingball>
                        </recipes>
                        """, oreDict, grinding, chance, power, durability, oreDict));
    }
}
