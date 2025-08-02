package com.tttsaurus.ometweaks.integration._omefactory.network;

import com.tttsaurus.ometweaks.integration._omefactory.network.packet.RespondPlayerOutsideSafeZone;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class OMEFactoryNetwork
{
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("OMEFactory");

    public static void sendPlayerSafeZonePacket(EntityPlayerMP player, int zoneLevel)
    {
        NETWORK.sendTo(new RespondPlayerOutsideSafeZone(zoneLevel), player);
    }

    public static void init()
    {
        NETWORK.registerMessage(RespondPlayerOutsideSafeZone.Handler.class, RespondPlayerOutsideSafeZone.class, 0, Side.CLIENT);
    }
}
