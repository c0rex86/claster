package me.c0re.claster.module.movement;

import me.c0re.claster.module.Category;
import me.c0re.claster.module.Module;
import net.minecraft.client.MinecraftClient;

public class AutoSprint extends Module {
    public AutoSprint() {
        super("AutoSprint", "Автоматически включает спринт при движении", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        if (mc.player.forwardSpeed > 0 && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.setSprinting(false);
        }
    }
} 