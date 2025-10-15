package com.portingdeadmods.modjam;

import com.mojang.logging.LogUtils;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBlockEntity;
import com.portingdeadmods.modjam.networking.MJNetworking;
import com.portingdeadmods.modjam.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

@Mod(Modjam.MODID)
public final class Modjam {
    public static final String MODID = "modjam";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Modjam(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        MJItems.ITEMS.register(modEventBus);
        MJBlocks.BLOCKS.register(modEventBus);
        MJBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        MJMenus.MENUS.register(modEventBus);
        MJCreativeTabs.CREATIVE_TABS.register(modEventBus);
        MJDataComponents.DATA_COMPONENTS.register(modEventBus);
        MJDataAttachments.ATTACHMENTS.register(modEventBus);
        MJRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        MJMultiblocks.MULTIBLOCKS.register(modEventBus);
        MJTranslations.TRANSLATIONS.register(modEventBus);

        modEventBus.addListener(MJNetworking::register);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(MJRegistries::registerDatapackRegistries);

        modContainer.registerConfig(ModConfig.Type.COMMON, MJConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(MJMultiblocks::init);
    }

    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MJBlockEntities.PLANET_SIMULATOR.get(), PlanetSimulatorBlockEntity::getItemHandlerReadOnly);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MJBlockEntities.PLANET_SIMULATOR.get(), PlanetSimulatorBlockEntity::getEnergyStorageReadOnly);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MJBlockEntities.PLANET_SIMULATOR.get(), PlanetSimulatorBlockEntity::getFluidHandlerReadOnly);

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MJBlockEntities.CREATIVE_POWER.get(), (be, side) -> be.getEnergyStorage());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MJBlockEntities.COMPRESSOR.get(), (be, side) -> be.getEnergyStorage());

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MJBlockEntities.ENERGY_INPUT_BUS.get(), (be, side) -> be.getEnergyStorage());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MJBlockEntities.ENERGY_OUTPUT_BUS.get(), (be, side) -> be.getEnergyStorage());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MJBlockEntities.ITEM_INPUT_BUS.get(), (be, side) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MJBlockEntities.ITEM_OUTPUT_BUS.get(), (be, side) -> be.getItemHandler());

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MJBlockEntities.FLUID_INPUT_BUS.get(), (be, side) -> be.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MJBlockEntities.FLUID_OUTPUT_BUS.get(), (be, side) -> be.getFluidTank());
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

}
