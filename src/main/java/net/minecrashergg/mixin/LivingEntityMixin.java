package net.minecrashergg.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecrashergg.HotdogHack;
import net.minecrashergg.modules.Jesus;
import net.minecrashergg.modules.SuperJump;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "canWalkOnFluid", at=@At("HEAD"), cancellable = true)
    void canWalkOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance().options.sneakKey.isPressed()) return;

        if (HotdogHack.isEnabled(Jesus.class)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method= "getJumpBoostVelocityModifier", at = @At("HEAD"), cancellable = true)
    void getJumpBoostVelocityModifier(CallbackInfoReturnable<Float> cir) {
        if (HotdogHack.isEnabled(SuperJump.class)) {
            cir.setReturnValue(SuperJump.jumpBoostMultiplier);
            cir.cancel();
        }
    }
}
