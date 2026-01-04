package com.skystudios.vamppatch.net;

import com.skystudios.vamppatch.VampPatch;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Net {
    public static SimpleNetworkWrapper CHANNEL;

    public static void init() {
        CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(VampPatch.MODID);
        CHANNEL.registerMessage(PacketStartFeed.Handler.class, PacketStartFeed.class, 0, Side.SERVER);
    }
}
