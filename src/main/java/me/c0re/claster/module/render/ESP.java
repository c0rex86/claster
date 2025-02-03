package me.c0re.claster.module.render;

import me.c0re.claster.module.Category;
import me.c0re.claster.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ESP extends Module {
    public ESP() {
        super("ESP", "Показывает сущности через стены", Category.RENDER);
    }

    public void renderOutline(MatrixStack matrices, Entity entity, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        Vec3d pos = entity.getPos().subtract(mc.gameRenderer.getCamera().getPos());
        Box box = entity.getBoundingBox().offset(pos);
        
        float red = entity instanceof LivingEntity ? 1.0f : 0.5f;
        float green = entity instanceof LivingEntity ? 0.0f : 0.5f;
        float blue = 0.0f;
        float alpha = 0.4f;

        drawOutlineBox(matrices, box, red, green, blue, alpha);
    }

    private void drawOutlineBox(MatrixStack matrices, Box box, float red, float green, float blue, float alpha) {
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer lines = immediate.getBuffer(RenderLayer.getLines());

        // Верхняя часть
        lines.vertex(matrices.peek().getPositionMatrix(), (float)box.minX, (float)box.maxY, (float)box.minZ).color(red, green, blue, alpha).normal(1, 0, 0).next();
        lines.vertex(matrices.peek().getPositionMatrix(), (float)box.maxX, (float)box.maxY, (float)box.minZ).color(red, green, blue, alpha).normal(1, 0, 0).next();
        
        // ... остальные линии бокса
        immediate.draw();
    }
} 