package net.minecrashergg.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecrashergg.HackManager;
import net.minecrashergg.modules.Hack;
import net.minecraft.util.Identifier;

/**
 * Button that toggles a hack, adds/removes it from HackManager,
 * and displays a custom texture for on/off state.
 */
public class ToggleHackButton extends ButtonWidget {
    private final Hack hackInstance;
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    private static final String BUTTON_TEXTURE_PATH = "textures/buttons/";

    private static final int BUTTON_HEIGHT = 40;  // double height
    private static final int BUTTON_WIDTH = 200;  // fixed width for uniformity


    public ToggleHackButton(Hack hackInstance) {
        super(
                0, 0,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                Text.literal(hackInstance.getFriendlyName()),
                button -> onPress(hackInstance),
                DEFAULT_NARRATION_SUPPLIER
        );
        this.hackInstance = hackInstance;
    }

    private static void onPress(Hack hackInstance) {
        hackInstance.toggle();

        if (hackInstance.isEnabled()) {
            HackManager.addHack(hackInstance);
        } else {
            HackManager.removeHack(hackInstance);
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Determine which texture to use based on enabled state
        String state = hackInstance.isEnabled() ? "on" : "off";
        String filename = hackInstance.getFriendlyName().toLowerCase() + "_" + state + ".png";
        //String filename = "test" + "_" + state + ".png";
        Identifier texture = Identifier.of("hotdogclient", BUTTON_TEXTURE_PATH+filename);
        // Default fallback
        Identifier fallbackTexture = Identifier.of("hotdogclient", BUTTON_TEXTURE_PATH+"default_" + state + ".png");

        var resourceManager = MinecraftClient.getInstance().getResourceManager();
        try {
            resourceManager.getResourceOrThrow(texture);
            // Attempt to draw custom texture
            context.drawTexture(RenderPipelines.GUI_TEXTURED,
                    texture,
                    this.getX(), this.getY(),
                    0, 0,
                    this.getWidth(), this.getHeight(),
                    this.getWidth(), this.getHeight());
        } catch (Exception e) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED,
                    fallbackTexture,
                    this.getX(), this.getY(),
                    0, 0,
                    this.getWidth(), this.getHeight(),
                    this.getWidth(), this.getHeight());
        }

        // Draw the text label on top
        //this.drawMessage(context, MC.textRenderer, 0xFFFFFFFF);
    }

    private static int getButtonWidth(Hack hackInstance) {
        if (MC == null || MC.textRenderer == null) return DEFAULT_WIDTH_SMALL;
        int textWidth = MC.textRenderer.getWidth(hackInstance.getFriendlyName());
        return Math.max(DEFAULT_WIDTH_SMALL, textWidth + 10);
    }
}
