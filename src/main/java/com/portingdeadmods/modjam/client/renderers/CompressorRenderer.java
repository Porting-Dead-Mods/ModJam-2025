package com.portingdeadmods.modjam.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.portingdeadmods.modjam.client.models.CompressorModel;
import com.portingdeadmods.modjam.content.blockentity.CompressorBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.client.renderers.blockentities.PDLBERenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CompressorRenderer extends PDLBERenderer<CompressorBlockEntity> {
    private final CompressorModel model;
    private final CompressorModel pressModel;

    public CompressorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = new CompressorModel(context.bakeLayer(CompressorModel.LAYER_LOCATION));
        this.pressModel = new CompressorModel(context.bakeLayer(CompressorModel.PRESS_LAYER_LOCATION));
    }

    @Override
    public void render(CompressorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float progress = 0.0f;
        int maxProgress = blockEntity.getMaxProgress();
        if (maxProgress > 0) {
            progress = ((float) blockEntity.getProgress() + partialTick) / (float) maxProgress;
            progress = Math.min(progress, 1.0f);
        }
        
        float pylonOffset = progress * 0.75f;
        
        Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rotation = switch (facing) {
            case NORTH -> 180f;
            case SOUTH -> 0f;
            case WEST -> 90f;
            case EAST -> 270f;
            default -> 0f;
        };
        
        poseStack.pushPose();
        {
            poseStack.translate(0.5, 0, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.translate(-0.5, 0, -0.5);
            
            poseStack.pushPose();
            {
                poseStack.translate(0.5, 1.5, 0.5);
                poseStack.mulPose(Axis.XN.rotationDegrees(180));
                this.model.renderToBuffer(poseStack, CompressorModel.MATERIAL.buffer(bufferSource, RenderType::entitySolid), LightTexture.FULL_BRIGHT, packedOverlay);
            }
            poseStack.popPose();
            poseStack.pushPose();
            {
                poseStack.translate(0.5, 2.5 - pylonOffset, 0.5);
                poseStack.mulPose(Axis.XN.rotationDegrees(180));
                this.pressModel.renderToBuffer(poseStack, CompressorModel.PRESS_MATERIAL.buffer(bufferSource, RenderType::entitySolid), LightTexture.FULL_BRIGHT, packedOverlay);
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

}
