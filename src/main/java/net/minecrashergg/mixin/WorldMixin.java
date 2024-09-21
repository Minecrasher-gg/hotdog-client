package net.minecrashergg.mixin;

import net.minecraft.world.World;
import net.minecrashergg.HotdogHack;
import net.minecrashergg.modules.NoWeather;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    private void getRainGradient(float delta, CallbackInfoReturnable<Float> cir) {
        if (HotdogHack.isEnabled(NoWeather.class)) {
            cir.setReturnValue(0.0f);
            cir.cancel();
        }
    }
}
