package com.portingdeadmods.modjam.client.renderers.blockentity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.client.renderers.PlanetModel;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBlockEntity;
import com.portingdeadmods.modjam.registries.MJShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Modjam.MODID, value = Dist.CLIENT) // munted
public class PlanetSimulatorBlockEntityRenderer implements BlockEntityRenderer<PlanetSimulatorBlockEntity> {

    static PlanetSimulatorBlockEntityRenderer INSTANCE;

    public static void prepareUniforms(float gridSize, float gridWidth, float noiseThreshold, float noiseScale, float noisePixelization, Vector3f noiseDirection, Vector3f gridColor, float flickerRate, float flickerIntensity)
    {
        MJShaders.PlanetProjectionShaderUniforms.GRID_SIZE.set(gridSize);
        MJShaders.PlanetProjectionShaderUniforms.GRID_WIDTH.set(gridWidth);
        MJShaders.PlanetProjectionShaderUniforms.NOISE_SCALE.set(noiseScale);
        MJShaders.PlanetProjectionShaderUniforms.NOISE_THRESHOLD.set(noiseThreshold);
        MJShaders.PlanetProjectionShaderUniforms.NOISE_PIXELIZATION.set(noisePixelization);
        MJShaders.PlanetProjectionShaderUniforms.NOISE_DIRECTION.set(noiseDirection);
        MJShaders.PlanetProjectionShaderUniforms.GRID_COLOR.set(gridColor);
        MJShaders.PlanetProjectionShaderUniforms.FLICKER_RATE.set(flickerRate);
        MJShaders.PlanetProjectionShaderUniforms.FLICKER_INTENSITY.set(flickerIntensity);

    }

    private record Batch(ResourceLocation texture, Matrix4f viewModel, float gridSize, float gridWidth, float noiseThreshold, float noiseScale, float noisePixelization, Vector3f noiseDirection, Vector3f gridColor, Vector4f tint, float flickerIntensity, float flickerRate)
    {
    }

    private List<Batch> batches = new ArrayList<>(10);

    MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(new ByteBufferBuilder(300));

    public VertexConsumer getBuffer(RenderType rt)
    {
        return this.bufferSource.getBuffer(rt);
    }

    /**
     * @param texture Texture location
     * @param pose posestack
     * @param gridSize amount of cells for each face
     * @param gridWidth how thick the borders of each cell are
     * @param noiseTreshhold how frequently noise appears range from 0 - -1
     * @param noiseScale how high resolution the noise is
     * @param noisePixelization how many pixels to quantize the noise to
     * @param noiseDirection how fast and what direction the noise is moving in
     * @param gridColor the color of the grid underneath
     * @param tint the color of the model
     * @param flickerIntensity how much flicker
     * @param flickerRate rate of flicker
     */
    public void renderPlanet(ResourceLocation texture, PoseStack pose, float gridSize,
                             float gridWidth, float noiseTreshhold, float noiseScale, float noisePixelization, Vector3f noiseDirection, Vector3f gridColor, Vector4f tint, float flickerIntensity, float flickerRate)
    {
        batches.add(new Batch(texture, new Matrix4f().mul(pose.last().pose()), gridSize, gridWidth, noiseTreshhold, 1 / noiseScale, noisePixelization, noiseDirection, gridColor, tint, flickerIntensity, flickerRate));
    }

    // gross transparency "fix"
    // TODO: fix planets not rendering behind each other
    @SubscribeEvent
    public static void renderLevelEvent(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {

            for (Batch batch : INSTANCE.batches)
            {
              //  RenderSystem.depthMask(true); // Rendertype doesn't correctly set depth mask state
                prepareUniforms(batch.gridSize, batch.gridWidth, batch.noiseThreshold, batch.noiseScale, batch.noisePixelization, batch.noiseDirection, batch.gridColor, batch.flickerIntensity, batch.flickerRate);
                VertexConsumer vcModel = INSTANCE.getBuffer(MJShaders.PLANET_PROJECTION.apply(batch.texture));

                PlanetModel.renderPlanetModelToBuffer(batch.viewModel, vcModel, batch.tint);

                INSTANCE.bufferSource.endLastBatch();

                VertexConsumer vcDepth = INSTANCE.getBuffer(RenderType.waterMask());

                PlanetModel.renderPlanetModelToBuffer(batch.viewModel, vcDepth, batch.tint);

                INSTANCE.bufferSource.endLastBatch();


            }
         //   INSTANCE.batches.clear();
            INSTANCE.batches = new ArrayList<>();
        }
    }

    public PlanetSimulatorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        INSTANCE = this;
    }

    private ResourceLocation getTexture(PlanetSimulatorBlockEntity be)
    {
        return Modjam.rl("textures/planet/ass.png");
    }

    @Override
    public void render(PlanetSimulatorBlockEntity planetSimulatorBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource _multiBufferSource, int combinedLight, int combinedOverlay) {

        if (    true) {
            poseStack.pushPose();
            Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
         //   VertexConsumer vc = _multiBufferSource.getBuffer(MJShaders.PLANET_PROJECTION.apply(getTexture(planetSimulatorBlockEntity)));
       //     prepareUniforms(8, 0.125f, 162, 126, new Vector3f(0, 0, 1), new Vector3f(0, 0, 1),0.00f, 0);
         //   PlanetModel.renderPlanetModelToBuffer(poseStack.last().pose(), vc, new Vector4f(1,1,1,1.0f));

       //     ( (MultiBufferSource.BufferSource)_multiBufferSource).endLastBatch();
            poseStack.translate(0.5F, 2.5F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees((Minecraft.getInstance().level.getGameTime() + partialTick)  * 2));



            this.renderPlanet(getTexture(planetSimulatorBlockEntity), poseStack, 16, 0.125f, -0.1f, 32, 4, new Vector3f(0, 0, 250f), new Vector3f(0.1f, 0.2f, 0.9f), new Vector4f(1, 1, 1.6f, 0.5f), 0.03f, 0);
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(PlanetSimulatorBlockEntity p_112306_) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(PlanetSimulatorBlockEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(PlanetSimulatorBlockEntity blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
    }
}
