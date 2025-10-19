package com.portingdeadmods.modjam.client.renderers.blockentity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.client.renderers.PlanetModel;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBlockEntity;
import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.modjam.registries.MJDataComponents;
import com.portingdeadmods.modjam.registries.MJMultiblocks;
import com.portingdeadmods.modjam.registries.MJShaders;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.core.Vec3i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Modjam.MODID, value = Dist.CLIENT) // munted
public class PlanetSimulatorBlockEntityRenderer implements BlockEntityRenderer<PlanetSimulatorBlockEntity> {

    private static final float PLANET_SIZE = 3.0f;
    private static final float PLANET_OFFSET_X = 0.0f;
    private static final float PLANET_OFFSET_Y = 4.5f;
    private static final float PLANET_OFFSET_Z = 0.0f;
    private static final float ROTATION_SPEED = 0.7f;
    private static final float BRIGHTNESS = 1.0f;

    static PlanetSimulatorBlockEntityRenderer INSTANCE;

    public static void prepareUniforms(float gridSize, float gridWidth, float noiseThreshold, float noiseScale, float noisePixelization, Vector3f noiseDirection, Vector3f gridColor, Vector4f tint, float flickerRate, float flickerIntensity, float progress)
    {
        MJShaders.PlanetProjectionShaderUniforms.GRID_SIZE.set(gridSize);
        MJShaders.PlanetProjectionShaderUniforms.GRID_WIDTH.set(gridWidth);
        MJShaders.PlanetProjectionShaderUniforms.NOISE_SCALE.set(noiseScale);
        MJShaders.PlanetProjectionShaderUniforms.NOISE_THRESHOLD.set(noiseThreshold);
        MJShaders.PlanetProjectionShaderUniforms.NOISE_PIXELIZATION.set(noisePixelization);
        MJShaders.PlanetProjectionShaderUniforms.NOISE_DIRECTION.set(noiseDirection);
        MJShaders.PlanetProjectionShaderUniforms.GRID_COLOR.set(gridColor);
        MJShaders.PlanetProjectionShaderUniforms.TINT.set(tint);
        MJShaders.PlanetProjectionShaderUniforms.FLICKER_RATE.set(flickerRate);
        MJShaders.PlanetProjectionShaderUniforms.FLICKER_INTENSITY.set(flickerIntensity);
        MJShaders.PlanetProjectionShaderUniforms.PROGRESS.set(progress);


    }

    private record Batch(ResourceLocation texture, Matrix4f viewModel, float gridSize, float gridWidth, float noiseThreshold, float noiseScale, float noisePixelization, Vector3f noiseDirection, Vector3f gridColor, Vector4f tint, float flickerIntensity, float flickerRate, float progress)
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
     * @param noiseTreshhold how frequently noise appears range from 0 to -1
     * @param noiseScale how high resolution the noise is
     * @param noisePixelization how many pixels to quantize the noise to
     * @param noiseDirection how fast and what direction the noise is moving in
     * @param gridColor the color of the grid underneath
     * @param tint the color of the model
     * @param flickerIntensity how much flicker
     * @param flickerRate rate of flicker
     * @param progress recipe progress range from 0 to 1
     */
    public void renderPlanet(ResourceLocation texture, PoseStack pose, float gridSize,
                             float gridWidth, float noiseTreshhold, float noiseScale, float noisePixelization, Vector3f noiseDirection, Vector3f gridColor, Vector4f tint, float flickerIntensity, float flickerRate, float progress)
    {
        batches.add(new Batch(texture, new Matrix4f().mul(pose.last().pose()), gridSize, gridWidth, noiseTreshhold, 1 / noiseScale, noisePixelization, noiseDirection, gridColor, tint, flickerIntensity, flickerRate, (progress - 0.5f) * 2));
    }

    // gross transparency "fix"
    // TODO: fix planets not rendering behind each other
    @SubscribeEvent
    public static void renderLevelEvent(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {

            for (Batch batch : INSTANCE.batches)
            {
              //  RenderSystem.depthMask(true); // Rendertype doesn't correctly set depth mask state
                prepareUniforms(batch.gridSize, batch.gridWidth, batch.noiseThreshold, batch.noiseScale, batch.noisePixelization, batch.noiseDirection, batch.gridColor, batch.tint, batch.flickerRate, batch.flickerIntensity, batch.progress);
                VertexConsumer vcModel = INSTANCE.getBuffer(MJShaders.PLANET_PROJECTION.apply(batch.texture));

                PlanetModel.renderPlanetModelToBuffer(batch.viewModel, vcModel, batch.tint);

                INSTANCE.bufferSource.endLastBatch();

                VertexConsumer vcDepth = INSTANCE.getBuffer(MJShaders.PLANET_PROJECTION_DEPTH);

                PlanetModel.renderPlanetModelToBuffer(batch.viewModel, vcDepth, batch.tint);

                  RenderSystem.depthMask(true); // Rendertype doesn't correctly set depth mask state
                RenderSystem.colorMask(false, false, false, false); // Rendertype doesn't correctly set depth mask state



                INSTANCE.bufferSource.endLastBatch();
                RenderSystem.colorMask(true, true, true, true); // Rendertype doesn't correctly set depth mask state


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
        if (be == null) return null;
        
        ItemStack planetCard = be.getItemHandler().getStackInSlot(0);
        if (planetCard.isEmpty() || !planetCard.has(MJDataComponents.PLANET)) {
            return null;
        }
        
        PlanetComponent planetComponent = planetCard.get(MJDataComponents.PLANET);
        if (planetComponent == null || planetComponent.planetType().isEmpty()) {
            return null;
        }
        
        return planetComponent.planetType().get().projectionTexture()
                .orElse(Modjam.rl("textures/planet/ass.png"));
    }
    
    private Vec3 getMultiblockCenter(PlanetSimulatorBlockEntity be) {
        if (be.getMultiblockData() == null) {
            return Vec3.atCenterOf(be.getBlockPos());
        }
        
        HorizontalDirection direction = be.getMultiblockData().direction();
        if (direction == null) {
            return Vec3.atCenterOf(be.getBlockPos());
        }
        
        Vec3i relativeControllerPos = MultiblockHelper.getRelativeControllerPos(MJMultiblocks.PLANET_SIMULATOR.get());
        BlockPos firstBlockPos = MultiblockHelper.getFirstBlockPos(direction, be.getBlockPos(), relativeControllerPos);
        
        Vec3i centerOffset = new Vec3i(3, 1, 3);
        BlockPos centerPos = MultiblockHelper.getCurPos(firstBlockPos, centerOffset, direction);
        
        return Vec3.atCenterOf(centerPos);
    }

    @Override
    public void render(PlanetSimulatorBlockEntity planetSimulatorBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource _multiBufferSource, int combinedLight, int combinedOverlay) {
        if (!planetSimulatorBlockEntity.getBlockState().getValue(Multiblock.FORMED)) {
            return;
        }
        
        ItemStack planetCard = planetSimulatorBlockEntity.getItemHandler().getStackInSlot(0);
        if (planetCard.isEmpty() || !planetCard.has(MJDataComponents.PLANET)) {
            return;
        }
        
        PlanetComponent planetComponent = planetCard.get(MJDataComponents.PLANET);
        if (planetComponent == null || planetComponent.planetType().isEmpty()) {
            return;
        }
        
        if (planetComponent.isBlackHole()) {
            Vec3 center = getMultiblockCenter(planetSimulatorBlockEntity);
            Vec3 controllerPos = Vec3.atLowerCornerOf(planetSimulatorBlockEntity.getBlockPos());
            Vec3 offset = center.subtract(controllerPos);
            Vector3f blackHolePos = new Vector3f((float)(offset.x + PLANET_OFFSET_X), (float)(offset.y + PLANET_OFFSET_Y), (float)(offset.z + PLANET_OFFSET_Z));
            BlackHoleExampleRenderer.blackholeUniformBuffer.blackhole(blackHolePos, 10, 15);
            return;
        }
        
        ResourceLocation texture = getTexture(planetSimulatorBlockEntity);
        if (texture == null) {
            return;
        }
        
        Vec3 center = getMultiblockCenter(planetSimulatorBlockEntity);
        Vec3 controllerPos = Vec3.atLowerCornerOf(planetSimulatorBlockEntity.getBlockPos());
        Vec3 offset = center.subtract(controllerPos);
        
        poseStack.pushPose();
        
        poseStack.translate(offset.x + PLANET_OFFSET_X, offset.y + PLANET_OFFSET_Y, offset.z + PLANET_OFFSET_Z);
        poseStack.mulPose(Axis.YP.rotationDegrees((Minecraft.getInstance().level.getGameTime() + partialTick) * ROTATION_SPEED));
        poseStack.scale(PLANET_SIZE, PLANET_SIZE, PLANET_SIZE);
        
        float progress = planetSimulatorBlockEntity.getMaxProgress() > 0 
            ? 1 - (float) planetSimulatorBlockEntity.getProgress() / (float) planetSimulatorBlockEntity.getMaxProgress()
            : 1.0f;
        
        this.renderPlanet(texture, poseStack, 16, 0.125f, -0.1f, 32, 4, new Vector3f(0, 0, 250f), new Vector3f(0.1f, 0.2f, 0.9f), new Vector4f(BRIGHTNESS, BRIGHTNESS, BRIGHTNESS, 0.5f), 0.05f, 533f, progress);
        poseStack.popPose();
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
        double planetSize = PLANET_SIZE;
        double rotationRadius = Math.sqrt(2 * (planetSize / 2) * (planetSize / 2));
        
        Vec3 center = getMultiblockCenter(blockEntity);
        Vec3 renderCenter = center.add(PLANET_OFFSET_X, PLANET_OFFSET_Y, PLANET_OFFSET_Z);
        
        return new AABB(
            renderCenter.x - rotationRadius,
            renderCenter.y - planetSize / 2,
            renderCenter.z - rotationRadius,
            renderCenter.x + rotationRadius,
            renderCenter.y + planetSize / 2,
            renderCenter.z + rotationRadius
        );
    }
}
