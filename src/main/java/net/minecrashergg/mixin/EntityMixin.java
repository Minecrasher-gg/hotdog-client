package net.minecrashergg.mixin;

import net.minecraft.entity.Entity;
import net.minecrashergg.HotdogHack;
import net.minecrashergg.modules.Step;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "getStepHeight", at = @At("HEAD"), cancellable = true)
    void getStepHeight(CallbackInfoReturnable<Float> cir) {
        if (HotdogHack.isEnabled(Step.class)) {
            cir.setReturnValue(Step.stepHeight);
            cir.cancel();
        }
    }
}
