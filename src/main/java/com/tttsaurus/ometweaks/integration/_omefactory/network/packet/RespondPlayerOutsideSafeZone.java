package com.tttsaurus.ometweaks.integration._omefactory.network.packet;

import com.tttsaurus.ometweaks.integration._omefactory.eventhandler.PlayerSafeZoneHudHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RespondPlayerOutsideSafeZone implements IMessage
{
    public RespondPlayerOutsideSafeZone() { }

    private int zoneLevel;

    public RespondPlayerOutsideSafeZone(int zoneLevel)
    {
        this.zoneLevel = zoneLevel;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        zoneLevel = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(zoneLevel);
    }

    public static class Handler implements IMessageHandler<RespondPlayerOutsideSafeZone, IMessage>
    {
        @Override
        public IMessage onMessage(RespondPlayerOutsideSafeZone message, MessageContext ctx)
        {
            if (!ctx.side.isClient()) return null;

            PlayerSafeZoneHudHandler.displayForOneMoreSec(message.zoneLevel);

            return null;
        }
    }
}
