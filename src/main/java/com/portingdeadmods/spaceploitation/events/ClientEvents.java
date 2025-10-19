package com.portingdeadmods.spaceploitation.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.client.renderers.blockentity.PlanetSimulatorBlockEntityRenderer;
import com.portingdeadmods.spaceploitation.client.screens.CompressorScreen;
import com.portingdeadmods.spaceploitation.client.screens.PlanetSimulatorScreen;
import com.portingdeadmods.spaceploitation.data.PlanetComponent;
import com.portingdeadmods.spaceploitation.registries.*;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = Spaceploitation.MODID, value = Dist.CLIENT)
public class ClientEvents {



    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                // The block entity type to register the renderer for.
                MJBlockEntities.PLANET_SIMULATOR.get(),
                // A function of BlockEntityRendererProvider.Context to BlockEntityRenderer.
                PlanetSimulatorBlockEntityRenderer::new
        );
    }

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
    //        capturedViewMatrix.translate((float) -cam.x, (float) -cam.y, (float) -cam.z);
        }

    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MJMenus.COMPRESSOR.get(), CompressorScreen::new);
        event.register(MJMenus.PLANET_SIMULATOR.get(), PlanetSimulatorScreen::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterShadersEvent event) {
        MJShaders.registerShaders(event);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex != 1) return 0xFFFFFFFF;

            PlanetComponent component = stack.get(MJDataComponents.PLANET);
            if (component == null || component.planetType().isEmpty()) {
                return 0xFFFFFFFF;
            }

            int color = component.planetType().get().tint().orElse(0xFFFFFF);
            return ensureOpaque(color);
        }, MJItems.TINTED_PLANET_CARD.get());
    }

    private static int ensureOpaque(int color) {
        return (color & 0xFF000000) == 0 ? 0xFF000000 | color : color;
    }

}
