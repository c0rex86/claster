package me.c0re.claster.ui.theme;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public class Theme {
    // Основные цвета
    public static final int BACKGROUND = 0x80000000;
    public static final int BACKGROUND_DARK = 0xB0000000;
    public static final int ACCENT = 0xFF00FFFF;
    public static final int ACCENT_DARK = 0xFF008B8B;
    public static final int TEXT = 0xFFFFFFFF;
    public static final int TEXT_DARK = 0xFFAAAAAA;

    // Анимация неонового свечения
    private static float glowIntensity = 0.0f;
    private static long lastTime = System.currentTimeMillis();

    public static void updateGlow() {
        long currentTime = System.currentTimeMillis();
        float delta = (currentTime - lastTime) / 1000.0f;
        lastTime = currentTime;
        
        glowIntensity = (float) ((Math.sin(System.currentTimeMillis() / 500.0) + 1.0) / 2.0);
    }

    public static int getGlowColor() {
        int r = 0;
        int g = (int) (255 * glowIntensity);
        int b = (int) (255 * glowIntensity);
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public static void drawNeonRect(DrawContext context, int x, int y, int width, int height, int color) {
        // Внутренний прямоугольник
        context.fill(x, y, x + width, y + height, color & 0x40FFFFFF);
        
        // Неоновая обводка
        int glowColor = getGlowColor();
        context.fill(x - 1, y - 1, x + width + 1, y, glowColor);
        context.fill(x - 1, y + height, x + width + 1, y + height + 1, glowColor);
        context.fill(x - 1, y, x, y + height, glowColor);
        context.fill(x + width, y, x + width + 1, y + height, glowColor);
    }

    public static void drawNeonText(DrawContext context, String text, float x, float y, boolean shadow) {
        int glowColor = getGlowColor();
        if (shadow) {
            context.drawText(MinecraftClient.getInstance().textRenderer, text, (int)x + 1, (int)y + 1, 0x40000000, false);
        }
        context.drawText(MinecraftClient.getInstance().textRenderer, text, (int)x, (int)y, glowColor, false);
    }
} 