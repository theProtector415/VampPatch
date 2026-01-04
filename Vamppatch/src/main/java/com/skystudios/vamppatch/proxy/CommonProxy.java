package com.skystudios.vamppatch.proxy;

import com.skystudios.vamppatch.net.Net;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        Net.init();
    }

    public void init(FMLInitializationEvent e) {
        // nothing needed here
    }
}
