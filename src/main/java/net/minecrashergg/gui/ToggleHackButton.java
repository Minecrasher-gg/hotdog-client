package net.minecrashergg.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.ColorHelper;
import net.minecrashergg.HotdogHack;
import net.minecrashergg.modules.Hack;

public class ToggleHackButton extends ButtonWidget {
    private final Class<? extends Hack> hackClass;

    public ToggleHackButton(Class<? extends Hack> hackClass) {
        super(0, 0,
            DEFAULT_WIDTH_SMALL, DEFAULT_HEIGHT,
            HotdogHack.getTranslatableText(hackClass),
            button -> HotdogHack.toggle(hackClass),
            DEFAULT_NARRATION_SUPPLIER
        );

        this.hackClass = hackClass;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(
            this.getX(), this.getY(),
            this.getX() + this.getWidth(), this.getY() + DEFAULT_HEIGHT,
            this.color()
        );

        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, 0xFFFFFFFF);
    }

    private int color() {
        int blue = HotdogHack.isEnabled(this.hackClass) ? 255 : 100;
        return ColorHelper.Argb.getArgb(255, 100, 100, blue);
    }
}
