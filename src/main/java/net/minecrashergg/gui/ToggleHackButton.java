package net.minecrashergg.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.ColorHelper;
import net.minecrashergg.HotdogHack;
import net.minecrashergg.modules.Hack;

public class ToggleHackButton extends ButtonWidget {
    private final Class<? extends Hack> hackClass;

    private static final int BASE_RED = 100;
    private static final int BASE_GREEN = 100;
    private static final int ENABLED_BLUE = 255;
    private static final int DISABLED_BLUE = 100;

    public ToggleHackButton(Class<? extends Hack> hackClass) {
        super(0, 0,
                getButtonWidth(hackClass), DEFAULT_HEIGHT,
                HotdogHack.getTranslatableText(hackClass),
                button -> HotdogHack.toggle(hackClass),
                DEFAULT_NARRATION_SUPPLIER
        );
        this.hackClass = hackClass;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int backgroundColor = this.color();
        context.fill(
                this.getX(), this.getY(),
                this.getX() + this.getWidth(), this.getY() + DEFAULT_HEIGHT,
                backgroundColor
        );
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, 0xFFFFFFFF);
    }

    private int color() {
        boolean isEnabled = HotdogHack.isEnabled(this.hackClass);
        int blue = isEnabled ? ENABLED_BLUE : DISABLED_BLUE;
        return ColorHelper.getArgb(255, BASE_RED, BASE_GREEN, blue);
    }

    private static int getButtonWidth(Class<? extends Hack> hackClass) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(HotdogHack.getTranslatableText(hackClass));
        return Math.max(DEFAULT_WIDTH_SMALL, textWidth + 10);  // Add padding for better UI
    }
}
