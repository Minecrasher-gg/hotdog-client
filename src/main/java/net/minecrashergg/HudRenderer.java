package net.minecrashergg;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class HudRenderer implements ClientModInitializer {
    private static final Identifier ID = Identifier.of("minecrashergg", "hud");

    int ActiveHacksindex = 0;

    private static final Identifier LOGO_TEXTURE =
            Identifier.of("hotdogclient", "textures/gui/hotdog_logo.png");

    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(ID, (context, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc == null || mc.textRenderer == null) return;

            TextRenderer tr = mc.textRenderer;
            String text = "Hotdog Client";

            //logo width and height
            int width = 40;
            int height = 40;

            // Background (optional: remove if not needed)
            int bgWidth = tr.getWidth(text) + 8;
            context.fill(4, 4, width + 12, height + 12, 0x90000000); // Logo background
            context.fill(width + 15, 4, width + bgWidth + 15, 12 + 4, 0x90000000); // Text background
            if (!HackManager.getActiveHacks().isEmpty()) {
                context.fill(4, height + 16, width + width / 3 + 8, height + 22 + ActiveHacksindex * 10, 0x90000000); // Active hacks background
            }

            // Switch to the "up layer" so text isn't hidden
            context.state.goUpLayer();


            // Draw the text and logo
            context.drawText(tr, text, width + 20, 6, Colors.WHITE, false);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, LOGO_TEXTURE, 8, 8, 0, 0, width, height, width, height);

            ActiveHacksindex = 0;
            for (String hackName : HackManager.getActiveHacks()) {

                int y = height + 20 + (ActiveHacksindex * 10); // 20px padding below logo
                context.drawText(
                        tr,
                        hackName,
                        7,
                        y,
                        Colors.WHITE,
                        false
                );

                ActiveHacksindex++;
            }

            // Go back down
            context.state.goDownLayer();
        });
    }
}
