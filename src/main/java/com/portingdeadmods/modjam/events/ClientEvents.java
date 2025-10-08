package com.portingdeadmods.modjam.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJShaders;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = Modjam.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    // gross hacks
    public static Matrix4f capturedProjectionMatrix;
    public static Matrix4f capturedViewMatrix;

    @SubscribeEvent
    public static void renderLevelEvent(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            capturedProjectionMatrix = new Matrix4f(RenderSystem.getProjectionMatrix());
            capturedViewMatrix = new Matrix4f(event.getModelViewMatrix());
            // testing
            Vec3 cam = event.getCamera().getPosition();
            capturedViewMatrix.translate((float) -cam.x, (float) -cam.y, (float) -cam.z);
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {

    }

    @SubscribeEvent
    public static void registerScreens(RegisterShadersEvent event) {
        MJShaders.registerShaders(event);
    }
}
