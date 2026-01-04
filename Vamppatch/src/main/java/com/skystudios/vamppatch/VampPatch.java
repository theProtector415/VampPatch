package com.skystudios.vamppatch;

import com.skystudios.vamppatch.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = VampPatch.MODID,
        name = VampPatch.NAME,
        version = VampPatch.VERSION,
        dependencies = "required-after:vampirism"
)
public class VampPatch {
    public static final String MODID = "vamppatch";
    public static final String NAME = "VampPatch";
    public static final String VERSION = "1.0";

    @SidedProxy(
            clientSide = "com.skystudios.vamppatch.proxy.ClientProxy",
            serverSide = "com.skystudios.vamppatch.proxy.CommonProxy"
    )
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        PROXY.init(e);
    }
}
