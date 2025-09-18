package net.minecrashergg.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecrashergg.HotdogHack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    /**
     * Make sure to give each hack the chance to change the packets
     * @param packet    the packet to modify
     * @param callbacks the callbacks for the packet
     * @param flush     should the function flush all the packets
     * @param ci        the callback info from the injection
     */
    @Inject(method = "send", at = @At("HEAD"), cancellable = true)
    private void onSend(Packet<?> packet, CallbackInfo ci) {
        HotdogHack.getHacks().forEach(hack -> {
            if (hack.isEnabled())
                if(hack.modifyPacket(packet)) {
                    ci.cancel();
                }
        });
    }
}
