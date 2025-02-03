package me.c0re.claster.module.combat;

import me.c0re.claster.module.Category;
import me.c0re.claster.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public class KillAura extends Module {
    private static final double ATTACK_RANGE = 4.0;
    private static final int ATTACK_DELAY = 10; // тиков
    private int tickCounter = 0;

    public KillAura() {
        super("KillAura", "Автоматически атакует ближайших существ", Category.COMBAT);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        tickCounter++;
        if (tickCounter < ATTACK_DELAY) return;
        tickCounter = 0;

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity target)) continue;
            if (target == mc.player) continue;
            if (!target.isAlive()) continue;
            if (mc.player.distanceTo(target) > ATTACK_RANGE) continue;

            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            break;
        }
    }
} 