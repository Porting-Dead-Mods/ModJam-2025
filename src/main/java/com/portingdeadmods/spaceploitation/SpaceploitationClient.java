package com.portingdeadmods.spaceploitation;

import com.portingdeadmods.spaceploitation.client.models.CompressorModel;
import com.portingdeadmods.spaceploitation.client.models.item.PlanetCardItemModel;
import com.portingdeadmods.spaceploitation.client.renderers.CompressorRenderer;
import com.portingdeadmods.spaceploitation.client.screens.EnergyBusScreen;
import com.portingdeadmods.spaceploitation.client.screens.FluidBusScreen;
import com.portingdeadmods.spaceploitation.client.screens.ItemBusScreen;
import com.portingdeadmods.spaceploitation.registries.MJBlockEntities;
import com.portingdeadmods.spaceploitation.registries.MJMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Spaceploitation.MODID, dist = Dist.CLIENT)
public final class SpaceploitationClient {
    public SpaceploitationClient(IEventBus modEventbus, ModContainer modContainer) {
        modEventbus.addListener(this::registerModelLayers);
        modEventbus.addListener(this::registerBERs);
        modEventbus.addListener(this::registerModelLoaders);
        modEventbus.addListener(this::registerScreens);

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
        event.register(Spaceploitation.rl("planet_card"), new PlanetCardItemModel.Loader());
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MJMenus.ITEM_BUS.get(), ItemBusScreen::new);
        event.register(MJMenus.ENERGY_BUS.get(), EnergyBusScreen::new);
        event.register(MJMenus.FLUID_BUS.get(), FluidBusScreen::new);
    }
}
