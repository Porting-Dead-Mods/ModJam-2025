package com.portingdeadmods.modjam;

import com.portingdeadmods.modjam.client.models.CompressorModel;
import com.portingdeadmods.modjam.client.models.item.PlanetCardItemModel;
import com.portingdeadmods.modjam.client.renderers.CompressorRenderer;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Modjam.MODID, dist = Dist.CLIENT)
public final class ModjamClient {
    public ModjamClient(IEventBus modEventbus, ModContainer modContainer) {
        modEventbus.addListener(this::registerModelLayers);
        modEventbus.addListener(this::registerBERs);
        modEventbus.addListener(this::registerModelLoaders);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CompressorModel.LAYER_LOCATION, CompressorModel::createBodyLayer);
        event.registerLayerDefinition(CompressorModel.PRESS_LAYER_LOCATION, CompressorModel::createPressBodyLayer);
    }

    private void registerBERs(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MJBlockEntities.COMPRESSOR.get(), CompressorRenderer::new);
    }

    public void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(Modjam.rl("planet_card"), new PlanetCardItemModel.Loader());
    }
}
