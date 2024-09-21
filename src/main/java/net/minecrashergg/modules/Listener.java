package net.minecrashergg.modules;

import net.minecraft.network.packet.Packet;
import net.minecrashergg.HotdogHack;

public class Listener extends Hack {
    @Override
    public boolean modifyPacket(Packet<?> packet) {
        HotdogHack.LOGGER.info("Sent Packet: " + packet.getClass());

        return false;
    }
}
