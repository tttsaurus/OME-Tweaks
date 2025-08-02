package com.tttsaurus.ometweaks.integration._omefactory.eventhandler;

import com.tttsaurus.ometweaks.integration._omefactory.OMEFactoryModule;
import com.tttsaurus.ometweaks.integration._omefactory.network.OMEFactoryNetwork;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.time.StopWatch;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PlayerSafeZoneHandler
{
    private final static Map<UUID, StopWatch> playerStopwatches = new HashMap<>();

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayerMP)
        {
            // refresh player cache
            List<UUID> outdated = new ArrayList<>();
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            for (UUID uuid: playerStopwatches.keySet())
            {
                EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(uuid);
                if (player == null)
                    outdated.add(uuid);
            }
            for (UUID uuid: outdated)
                playerStopwatches.remove(uuid);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        List<EntityPlayerMP> players = server.getPlayerList().getPlayers();

        for (EntityPlayerMP player: players)
        {
            if (!OMEFactoryModule.GAMESTAGE_TO_DISABLE_SAFE_ZONE.isEmpty() && OMEFactoryModule.IS_GAMESTAGES_LOADED)
                if (GameStageHelper.hasStage(player, OMEFactoryModule.GAMESTAGE_TO_DISABLE_SAFE_ZONE))
                    continue;
            if (player.isCreative()) continue;

            float norm = (float)Math.sqrt(player.posX * player.posX + player.posZ * player.posZ);

            // early escape. player is inside the inner safe zone
            if (norm < OMEFactoryModule.PLAYER_SAFE_ZONE_RADIUS * 2f / 3f)
            {
                playerStopwatches.remove(player.getUniqueID());
                continue;
            }

            AtomicBoolean execute = new AtomicBoolean(false);
            StopWatch stopWatch = playerStopwatches.computeIfAbsent(player.getUniqueID(), (k) ->
            {
                StopWatch output = new StopWatch();
                output.start();
                execute.set(true);
                return output;
            });
            if (stopWatch.getNanoTime() / 1e9d >= 0.9d)
            {
                execute.set(true);
                stopWatch.stop();
                stopWatch.reset();
                stopWatch.start();
            }

            if (execute.get())
            {
                if (norm >= OMEFactoryModule.PLAYER_SAFE_ZONE_RADIUS)
                {
                    for (PotionEffect potionEffect: OMEFactoryModule.SAFE_ZONE_OUTSIDE_EFFECTS)
                        player.addPotionEffect(new PotionEffect(potionEffect));
                    OMEFactoryNetwork.sendPlayerSafeZonePacket(player, 2);
                }
                else if (norm >= OMEFactoryModule.PLAYER_SAFE_ZONE_RADIUS * 2f / 3f)
                {
                    OMEFactoryNetwork.sendPlayerSafeZonePacket(player, 1);
                }
            }
        }
    }
}
