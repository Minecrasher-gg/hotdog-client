package net.minecrashergg.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.ColorHelper;
import net.minecrashergg.HackManager;
import net.minecrashergg.HotdogHack;
import net.minecrashergg.modules.Hack;

/**
 * Button that toggles a hack class. Uses HotdogHack helpers for text and toggling,
 * and updates the HackManager list when a hack is enabled/disabled.
 */
public class ToggleHackButton extends ButtonWidget {
    private final Class<? extends Hack> hackClass;

    private static final int BASE_RED = 100;
    private static final int BASE_GREEN = 100;
    private static final int ENABLED_BLUE = 255;
    private static final int DISABLED_BLUE = 100;

    public ToggleHackButton(Class<? extends Hack> hackClass) {
        super(
                0, 0,
                getButtonWidth(hackClass),
                DEFAULT_HEIGHT,
                HotdogHack.getTranslatableText(hackClass),            // keep old Text-based label
                button -> onPress(hackClass),
                DEFAULT_NARRATION_SUPPLIER
        );
        this.hackClass = hackClass;
    }

    private static void onPress(Class<? extends Hack> hackClass) {
        // Toggle via the existing helper (delegates to HackManager.getHack/toggle)
        HotdogHack.toggle(hackClass);

        // Resolve the instance and add/remove to our HackManager list
        Hack instance = HotdogHack.getHack(hackClass);
        if (instance != null) {
            if (instance.isEnabled()) {
                HackManager.addHack(instance);
            } else {
                HackManager.removeHack(instance);
            }
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int backgroundColor = color();
        context.fill(
                this.getX(), this.getY(),
                this.getX() + this.getWidth(), this.getY() + DEFAULT_HEIGHT,
                backgroundColor
        );

        // draw the label (keeps same behavior as your original)
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, 0xFFFFFFFF);
    }

    private int color() {
        boolean isEnabled = HotdogHack.isEnabled(this.hackClass);
        int blue = isEnabled ? ENABLED_BLUE : DISABLED_BLUE;
        return ColorHelper.getArgb(255, BASE_RED, BASE_GREEN, blue);
    }

    private static int getButtonWidth(Class<? extends Hack> hackClass) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.textRenderer == null) return DEFAULT_WIDTH_SMALL;
        int textWidth = mc.textRenderer.getWidth(HotdogHack.getTranslatableText(hackClass));
        return Math.max(DEFAULT_WIDTH_SMALL, textWidth + 10);
    }
}
