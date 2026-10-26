package gg.krypzo.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class KrypzoClient implements ClientModInitializer {
    public static final String MOD_ID = "krypzo";
    public static boolean hudEnabled = true;
    public static boolean keystrokesEnabled = true;
    public static boolean coordinatesEnabled = true;
    public static boolean zoomEnabled = false;
    public static boolean baseFinderEnabled = false;
    public static boolean xrayNotesEnabled = false;
    public static boolean counterFpsEnabled = true;
    public static boolean antiKickDiagnostics = true;
    public static double zoomFactor = 3.0;

    private static KeyMapping.Category category;
    private static KeyMapping clickGuiKey;
    private static KeyMapping zoomKey;

    @Override
    public void onInitializeClient() {
        category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));
        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.krypzo.clickgui", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));
        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.krypzo.zoom", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (clickGuiKey.consumeClick()) {
                if (client.player != null) client.gui.setScreen(new KrypzoScreen());
            }
            zoomEnabled = zoomKey.isDown();
        });

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(MOD_ID, "hud"), KrypzoClient::renderHud);
    }

    private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (!hudEnabled || client.options.hideGui || client.player == null) return;

        int width = client.getWindow().getGuiScaledWidth();
        int fps = client.getFps();
        int y = 8;

        // Upper-right module panel
        graphics.fill(width - 146, 6, width - 6, 18 + enabledCount() * 12, 0xB9101220);
        graphics.drawString(client.font, Component.literal("K R Y P Z O"), width - 138, y, 0xFF8B5CF6);
        y += 14;
        String[] modules = {"Keystrokes", "Coordinates", "Zoom", "BaseFinder", "Xray notes"};
        boolean[] states = {keystrokesEnabled, coordinatesEnabled, zoomEnabled, baseFinderEnabled, xrayNotesEnabled};
        for (int i = 0; i < modules.length; i++) {
            if (states[i]) {
                graphics.drawString(client.font, Component.literal("• " + modules[i]), width - 138, y, 0xFFE5E7EB);
                y += 12;
            }
        }

        // FPS bottom-left
        if (counterFpsEnabled) {
            graphics.fill(6, graphics.guiHeight() - 20, 78, graphics.guiHeight() - 6, 0xB9101220);
            graphics.drawString(client.font, Component.literal("FPS " + fps), 11, graphics.guiHeight() - 17, 0xFF67E8F9);
        }

        // Coordinates and a simple keystrokes status display
        if (coordinatesEnabled) {
            String coords = String.format("XYZ %.1f / %.1f / %.1f",
                    client.player.getX(), client.player.getY(), client.player.getZ());
            graphics.drawString(client.font, Component.literal(coords), 8, graphics.guiHeight() - 34, 0xFFFFFFFF);
        }
        if (keystrokesEnabled) {
            graphics.drawString(client.font, Component.literal("W A S D"), 8, graphics.guiHeight() - 48, 0xFFBCA7FF);
        }
        if (zoomEnabled) {
            graphics.drawString(client.font, Component.literal("ZOOM x" + zoomFactor + " (visual prototype)"),
                    width / 2 - 65, 16, 0xFF8B5CF6);
        }
    }

    private static int enabledCount() {
        int count = 1;
        if (keystrokesEnabled) count++;
        if (coordinatesEnabled) count++;
        if (zoomEnabled) count++;
        if (baseFinderEnabled) count++;
        if (xrayNotesEnabled) count++;
        return count;
    }
}
