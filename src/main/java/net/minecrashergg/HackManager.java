package net.minecrashergg;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecrashergg.modules.AntiHunger;
import net.minecrashergg.modules.Flight;
import net.minecrashergg.modules.FullBright;
import net.minecrashergg.modules.Hack;
import net.minecrashergg.modules.Jesus;
import net.minecrashergg.modules.KillAura;
import net.minecrashergg.modules.NoFall;
import net.minecrashergg.modules.NoWeather;
import net.minecrashergg.modules.Speed;
import net.minecrashergg.modules.Step;
import net.minecrashergg.modules.SuperJump;
import net.minecrashergg.modules.XRay;

public class HackManager {
    private static final List<Hack> hacks = new ArrayList<>();

    public static void init() {
        hacks.add(new Flight());
        hacks.add(new NoFall());
		hacks.add(new Speed());
		hacks.add(new Step());
		hacks.add(new Jesus());
		hacks.add(new SuperJump());
		hacks.add(new FullBright());
		hacks.add(new XRay());
		hacks.add(new NoWeather());
		hacks.add(new AntiHunger());
		hacks.add(new KillAura());

        ClientTickEvents.START_CLIENT_TICK.register(client ->
            hacks.forEach(hack -> {
                if (hack.isEnabled()) hack.tick();
            })
        );
        ClientTickEvents.END_CLIENT_TICK.register(client ->
            hacks.forEach(hack -> {
                if (hack.isEnabled()) hack.postTick();
            })
        );
    }

    public static <T extends Hack> T getHack(Class<T> hackClass) {
        for (Hack hack : hacks) {
            if (hack.getClass() == hackClass) {
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

    public static List<Hack> getHacks() {
        return hacks;
    }

    public static int numHacks() {
        return hacks.size();
    }
}
