package net.minecrashergg;

import net.fabricmc.api.ModInitializer;

public class HotdogHack implements ModInitializer {
    @Override
    public void onInitialize() {
        HackManager.init();
        HudRenderer.init();
        MusicManager.init();
        CommandHandler.init();
    }
}