package net.minecrashergg;

import net.minecrashergg.modules.Hack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HackManager {
    // List of currently active hacks
    private static final List<String> activeHacks = new ArrayList<>();

    /**
     * Adds a hack to the active list using a simplified name.
     */
    public static void addHack(Hack hack) {
        if (hack == null) return;
        String name = toFriendlyName(hack);
        if (!activeHacks.contains(name)) {
            activeHacks.add(name);
        }
    }

    /**
     * Removes a hack from the active list.
     */
    public static void removeHack(Hack hack) {
        if (hack == null) return;
        String name = toFriendlyName(hack);
        activeHacks.remove(name);
    }

    /**
     * Returns an unmodifiable list of active hack names (simplified names).
     */
    public static List<String> getActiveHacks() {
        return Collections.unmodifiableList(activeHacks);
    }

    /**
     * Converts a hack instance to a simplified lowercase name.
     */
    private static String toFriendlyName(Hack hack) {
        String className = hack.getClass().getSimpleName();
        return className.toLowerCase(); // e.g., "Speed" -> "speed"
    }
}
