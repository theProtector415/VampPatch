package com.skystudios.vamppatch.client;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Keybinds {
    public static KeyBinding FEED_KEY;

    public static void init() {
        // Default V; you can change later in Controls menu
        FEED_KEY = new KeyBinding("key.vamppatch.feed", Keyboard.KEY_V, "key.categories.vamppatch");
    }
}
