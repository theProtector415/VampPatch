package com.skystudios.vamppatch.client;

import com.skystudios.vamppatch.net.Net;
import com.skystudios.vamppatch.net.PacketStartFeed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientFeedingController {

    private int feedingTicks = 0;

    private static final int MIN_TICKS_ON_START = 8;
    private static final float FOV_MULT = 0.75f;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        EntityPlayerSP player = mc.player;

        if (feedingTicks > 0) feedingTicks--;

        if (Keybinds.FEED_KEY.isKeyDown()) {
            Entity target = getLookedAtEntity(mc);
            if (target != null) {
                feedingTicks = Math.max(feedingTicks, MIN_TICKS_ON_START);

                if (player.ticksExisted % 3 == 0) {
                    Net.CHANNEL.sendToServer(new PacketStartFeed(target.getEntityId()));
                }
            }
        }

        // Lock movement while "feeding"
        if (feedingTicks > 0 && player.movementInput != null) {
            player.movementInput.moveForward = 0;
            player.movementInput.moveStrafe = 0;
            player.movementInput.jump = false;
            player.movementInput.sneak = false;
        }
    }

    @SubscribeEvent
    public void onFov(FOVUpdateEvent e) {
        if (feedingTicks > 0) {
            e.setNewfov(e.getNewfov() * FOV_MULT);
        }
    }

    private Entity getLookedAtEntity(Minecraft mc) {
        RayTraceResult rtr = mc.objectMouseOver;
        if (rtr == null) return null;
        if (rtr.typeOfHit == RayTraceResult.Type.ENTITY) return rtr.entityHit;
        return null;
    }
}
