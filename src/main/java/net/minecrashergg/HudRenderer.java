package net.minecrashergg;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecrashergg.modules.Hack;

public class HudRenderer {
    public static void init() {
        HudRenderCallback.EVENT.register((drawContext, delta) -> {
            if (!MinecraftClient.getInstance().getDebugHud().shouldShowDebugHud())
                drawHud(drawContext);
        });
    }

    private static void drawHud(DrawContext ctx) {
        var player = MinecraftClient.getInstance().player;
        if (player == null) return;

        var textRenderer = MinecraftClient.getInstance().textRenderer;
        drawWatermark(ctx, textRenderer, player.age);

        int index = 1;
        for (Hack hack : HackManager.getHacks()) {
            if (!hack.isEnabled()) continue;
            float hue = ((float) player.age / 200f + (float) index / (HackManager.numHacks() + 1)) % 1f;
            int color = MathHelper.hsvToRgb(hue, 1f, 1f);

            ctx.drawText(textRenderer,
                    Text.translatable("hacks." + hack.getClass().getSimpleName().toLowerCase()),
                    10, 25 + index * 10, color, true);
            index++;
        }
    }

    private static void drawWatermark(DrawContext ctx, TextRenderer renderer, int age) {
        // Keep your cool rainbow/scale watermark drawing here.
    }
}
