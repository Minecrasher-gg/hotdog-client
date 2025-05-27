package net.minecrashergg;

import net.minecrashergg.modules.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HotdogHack implements ModInitializer {
    public static final String MOD_ID = "hotdogclient";

    public static final Logger LOGGER = LoggerFactory.getLogger("HotdogHack");

    private static final List<Hack> hacks = new ArrayList<>();

    @Override
    public void onInitialize() {
        HudRenderer.init();
        hacks.add(new XRay());
        hacks.add(new FullBright());
        hacks.add(new Speed());
        hacks.add(new Step());
        hacks.add(new Jesus());
        hacks.add(new NoWeather());
        hacks.add(new SuperJump());
        hacks.add(new AntiHunger());
        hacks.add(new Flight());
        hacks.add(new KillAura());
        hacks.add(new NoFall());

        LOGGER.info("Registered {} hacks.", hacks.size());
    }

    public static List<Hack> getHacks() {
        return hacks;
    }

    public static <T extends Hack> T getHack(Class<T> hackClass) {
        for (Hack hack : hacks) {
            if (hack.getClass().equals(hackClass)) {
                return hackClass.cast(hack);
            }
        }
        return null;
    }

    public static boolean isEnabled(Class<? extends Hack> hackClass) {
        Hack hack = getHack(hackClass);
        return hack != null && hack.isEnabled();
    }

    public static void toggle(Class<? extends Hack> hackClass) {
        Hack hack = getHack(hackClass);
        if (hack != null) {
            hack.toggle();
        }
    }

    public static Text getTranslatableText(Class<? extends Hack> hackClass) {
        return Text.translatable("hotdoghack." + hackClass.getSimpleName().toLowerCase());
    }
}