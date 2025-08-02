package com.tttsaurus.ometweaks.integration._omefactory.eventhandler;

import com.tttsaurus.ometweaks.gui.GuiResources;
import com.tttsaurus.ometweaks.render.RenderUtils;
import com.tttsaurus.ometweaks.utils.SmoothDamp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.time.StopWatch;
import java.awt.*;

public final class PlayerSafeZoneHudHandler
{
    private static int zoneLevel;
    private static StopWatch stopWatch = null;

    private static boolean in = false;
    private static boolean out = false;
    private static boolean closed = true;

    private static final float DURATION = 0.5f;
    private static float lastTime;
    private static StopWatch smoothDampStopWatch = null;
    private static SmoothDamp smoothDamp = null;

    public static void displayForOneMoreSec(int zoneLevel)
    {
        PlayerSafeZoneHudHandler.zoneLevel = zoneLevel;
        stopWatch = new StopWatch();
        stopWatch.start();
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        if (stopWatch == null && closed && !out) return;

        if (stopWatch != null && stopWatch.getNanoTime() / 1e9d > 1d && !out)
        {
            stopWatch = null;
            closed = true;
            in = false;
            out = true;
            smoothDamp = new SmoothDamp(0, -30, DURATION);
        }

        if (stopWatch != null && closed && !out)
        {
            closed = false;
            in = true;
            smoothDamp = new SmoothDamp(-30, 0, DURATION);
        }

        float offset = 0;

        float deltaTime = 0;
        if (smoothDamp != null)
        {
            if (smoothDampStopWatch == null)
            {
                smoothDampStopWatch = new StopWatch();
                smoothDampStopWatch.start();
                lastTime = 0;
            }
            float time = (float)(smoothDampStopWatch.getNanoTime() / 1e9d);
            deltaTime = time - lastTime;
            lastTime = time;
        }

        if (in)
        {
            offset = smoothDamp.evaluate(deltaTime);
            if (smoothDampStopWatch.getNanoTime() / 1e9d >= DURATION)
            {
                smoothDamp = null;
                smoothDampStopWatch = null;
                in = false;
            }
        }

        if (out)
        {
            offset = smoothDamp.evaluate(deltaTime);
            if (smoothDampStopWatch.getNanoTime() / 1e9d >= DURATION)
            {
                smoothDamp = null;
                smoothDampStopWatch = null;
                out = false;
            }
        }

        String tip = "";
        int color = -1;
        if (zoneLevel == 1)
        {
            tip = I18n.format("omefactory.safe_zone.str2");
            color = (new Color(0x5E48C2)).getRGB();
        }
        else if (zoneLevel == 2)
        {
            tip = I18n.format("omefactory.safe_zone.str1");
            color = (new Color(0xA833C1)).getRGB();
        }

        int padding = 5;
        int width = RenderUtils.fontRenderer.getStringWidth(tip) + padding * 2;
        int height = RenderUtils.fontRenderer.FONT_HEIGHT + padding * 2;

        RenderUtils.storeCommonGlStates();

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtils.renderImagePrefab((resolution.getScaledWidth() - width) / 2f, 12 + offset, width, height, GuiResources.get("vanilla_background"));

        RenderUtils.renderText(tip, (resolution.getScaledWidth() - width) / 2f + padding, 12 + padding + offset, 1f, color, false);

        RenderUtils.restoreCommonGlStates();
    }
}
