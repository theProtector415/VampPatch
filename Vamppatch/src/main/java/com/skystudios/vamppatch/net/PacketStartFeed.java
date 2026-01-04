package com.skystudios.vamppatch.net;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.IBiteableEntity;
import de.teamlapen.vampirism.api.entity.IExtendedCreatureVampirism;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStartFeed implements IMessage {
    private int targetId;

    public PacketStartFeed() {}
    public PacketStartFeed(int targetId) { this.targetId = targetId; }

    @Override
    public void fromBytes(ByteBuf buf) { this.targetId = buf.readInt(); }

    @Override
    public void toBytes(ByteBuf buf) { buf.writeInt(targetId); }

    public static class Handler implements IMessageHandler<PacketStartFeed, IMessage> {
        @Override
        public IMessage onMessage(PacketStartFeed msg, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> tryFeed(player, msg.targetId));
            return null;
        }

        private void tryFeed(EntityPlayerMP player, int targetId) {
            Entity target = player.world.getEntityByID(targetId);
            if (target == null) return;

            // --- Get current faction player and ensure it's a vampire player ---
            IFactionPlayerHandler handler = VampirismAPI.getFactionPlayerHandler(player);
            if (handler == null) return;

            IFactionPlayer fp = handler.getCurrentFactionPlayer();
            if (!(fp instanceof IVampirePlayer)) return;

            IVampirePlayer vamp = (IVampirePlayer) fp;

            // If the vampire doesn't want blood, don't feed
            if (!vamp.wantsBlood()) return;

            // --- Resolve biteable target ---
            IBiteableEntity biteable = null;

            if (target instanceof EntityCreature) {
                IExtendedCreatureVampirism ext =
                        VampirismAPI.getExtendedCreatureVampirism((EntityCreature) target);
                if (ext != null) biteable = ext;
            }

            if (biteable == null && target instanceof IBiteableEntity) {
                biteable = (IBiteableEntity) target;
            }

            if (biteable == null) return;
            if (!biteable.canBeBitten(vamp)) return;

            int gained = biteable.onBite(vamp);
            if (gained <= 0) return;

            float sat = biteable.getBloodSaturation();
            vamp.drinkBlood(gained, sat, true);
        }
    }
}
