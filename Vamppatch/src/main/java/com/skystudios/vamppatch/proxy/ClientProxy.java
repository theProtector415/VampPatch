package com.skystudios.vamppatch.proxy;

import com.skystudios.vamppatch.client.ClientFeedingController;
import com.skystudios.vamppatch.client.Keybinds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        Keybinds.init();
        ClientRegistry.registerKeyBinding(Keybinds.FEED_KEY);

        MinecraftForge.EVENT_BUS.register(new ClientFeedingController());
    }
}
