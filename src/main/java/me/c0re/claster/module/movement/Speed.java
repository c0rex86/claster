package me.c0re.claster.module.movement;

import me.c0re.claster.module.Category;
import me.c0re.claster.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {
    private static final double SPEED_MULTIPLIER = 1.5;

    public Speed() {
        super("Speed", "Увеличивает скорость передвижения", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || !mc.player.isOnGround()) return;

        if (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0) {
            Vec3d velocity = mc.player.getVelocity();
            mc.player.setVelocity(velocity.x * SPEED_MULTIPLIER, 
                                velocity.y, 
                                velocity.z * SPEED_MULTIPLIER);
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.setVelocity(mc.player.getVelocity().multiply(1.0 / SPEED_MULTIPLIER, 1, 1.0 / SPEED_MULTIPLIER));
        }
    }
} 