package gg.krypzo.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class KrypzoScreen extends Screen {
    private static final int PANEL_W = 310;
    private static final int PANEL_H = 248;

    public KrypzoScreen() {
        super(Component.literal("Krypzo Client"));
    }

    @Override
    protected void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - PANEL_H) / 2;

        graphics.fill(0, 0, this.width, this.height, 0x99000000);
        graphics.fill(x, y, x + PANEL_W, y + PANEL_H, 0xF0101220);
        graphics.fill(x, y, x + PANEL_W, y + 34, 0xFF20183A);
        graphics.drawString(this.font, Component.literal("K R Y P Z O   C L I E N T"), x + 12, y + 12, 0xFFBCA7FF);
        graphics.drawString(this.font, Component.literal("Left click: toggle  |  Right click: settings"), x + 12, y + 42, 0xFF9CA3AF);

        String[] names = {"HUD", "Keystrokes", "FPS Counter", "Coordinates", "Zoom", "BaseFinder (authorized worlds)", "Xray notes (no block reveal)", "AntiKick diagnostics"};
        boolean[] enabled = {
                KrypzoClient.hudEnabled, KrypzoClient.keystrokesEnabled, KrypzoClient.counterFpsEnabled,
                KrypzoClient.coordinatesEnabled, KrypzoClient.zoomEnabled, KrypzoClient.baseFinderEnabled,
                KrypzoClient.xrayNotesEnabled, KrypzoClient.antiKickDiagnostics
        };
        for (int i = 0; i < names.length; i++) {
            int rowY = y + 62 + i * 20;
            graphics.fill(x + 10, rowY - 3, x + PANEL_W - 10, rowY + 14,
                    enabled[i] ? 0xFF24213A : 0xFF171923);
            graphics.drawString(this.font, Component.literal((enabled[i] ? "[ON]  " : "[OFF] ") + names[i]),
                    x + 17, rowY, enabled[i] ? 0xFF86EFAC : 0xFFE5E7EB);
        }
        graphics.drawString(this.font, Component.literal("Right-click module rows for settings (where available)."),
                x + 12, y + PANEL_H - 18, 0xFF9CA3AF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width - PANEL_W) / 2;
        int y = (this.height - PANEL_H) / 2;
        int index = (int) ((mouseY - (y + 59)) / 20);
        if (mouseX >= x + 10 && mouseX <= x + PANEL_W - 10 && index >= 0 && index < 8) {
            if (button == 1) {
                Minecraft.getInstance().player.displayClientMessage(
                        Component.literal("Krypzo: no configurable settings for this module yet."), true);
                return true;
            }
            if (button == 0) {
                switch (index) {
                    case 0 -> KrypzoClient.hudEnabled = !KrypzoClient.hudEnabled;
                    case 1 -> KrypzoClient.keystrokesEnabled = !KrypzoClient.keystrokesEnabled;
                    case 2 -> KrypzoClient.counterFpsEnabled = !KrypzoClient.counterFpsEnabled;
                    case 3 -> KrypzoClient.coordinatesEnabled = !KrypzoClient.coordinatesEnabled;
                    case 4 -> KrypzoClient.zoomEnabled = !KrypzoClient.zoomEnabled;
                    case 5 -> KrypzoClient.baseFinderEnabled = !KrypzoClient.baseFinderEnabled;
                    case 6 -> KrypzoClient.xrayNotesEnabled = !KrypzoClient.xrayNotesEnabled;
                    case 7 -> KrypzoClient.antiKickDiagnostics = !KrypzoClient.antiKickDiagnostics;
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
