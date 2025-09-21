package net.minecrashergg.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecrashergg.gui.HackScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
	protected GameMenuScreenMixin(Text title) {
		super(title);
	}

	/**
	 * Create and add the button to go to the screen to enable/disable hacks
	 * @param ci the callback info from the injection
	 */
    @Inject(method = "initWidgets", at = @At("HEAD"))
    void initWidgets(CallbackInfo ci) {
        int buttonWidth = 204;
        int buttonHeight = 20;

        // Center horizontally
        int x = (this.width - buttonWidth) / 2;

        // Place vertically just above the first vanilla button
        // Typically, the first button is at this.height / 4 + offset
        // We'll use this.height / 4 - buttonHeight - 4 as a safe margin
        int y = this.height / 4 - buttonHeight + 4;

        var hackButton = ButtonWidget.builder(
                Text.translatable("hacks.title"),
                button -> MinecraftClient.getInstance().setScreen(new HackScreen())
        ).position(x, y).size(buttonWidth, buttonHeight).build();

        this.addDrawableChild(hackButton);
    }
}