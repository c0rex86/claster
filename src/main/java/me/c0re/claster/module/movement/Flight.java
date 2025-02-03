package me.c0re.claster.module.movement;

import me.c0re.claster.module.Category;
import me.c0re.claster.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class Flight extends Module {
    private static final double FLY_SPEED = 0.8;

    public Flight() {
        super("Flight", "Позволяет летать", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlySpeed((float) FLY_SPEED);

        if (mc.options.jumpKey.isPressed()) {
            mc.player.setVelocity(mc.player.getVelocity().add(0, FLY_SPEED, 0));
        }
        if (mc.options.sneakKey.isPressed()) {
            mc.player.setVelocity(mc.player.getVelocity().add(0, -FLY_SPEED, 0));
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null && !mc.player.getAbilities().creativeMode) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().setFlySpeed(0.05f);
        }
    }
} 