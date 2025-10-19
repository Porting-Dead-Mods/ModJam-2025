package com.portingdeadmods.modjam.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.portingdeadmods.modjam.render.BlackHoleUniformBufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class BlackHoleExampleRenderer implements BlockEntityRenderer<ChestBlockEntity> {

    public static final BlackHoleUniformBufferBuilder blackholeUniformBuffer = new BlackHoleUniformBufferBuilder();

    @Override
    public void render(ChestBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        //blackholeUniformBuffer.blackhole(new Vector3f(0, 100, 0), 10, 15);
    }

    @Override
    public boolean shouldRenderOffScreen(ChestBlockEntity p_112306_) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(p_112306_);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(ChestBlockEntity blockEntity, Vec3 cameraPos) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }

    @Override
    public AABB getRenderBoundingBox(ChestBlockEntity blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}
