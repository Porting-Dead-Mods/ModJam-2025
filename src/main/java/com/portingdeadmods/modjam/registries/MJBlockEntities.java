package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.blockentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MJBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Modjam.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlanetSimulatorBlockEntity>> PLANET_SIMULATOR =
            BLOCK_ENTITIES.register("planet_simulator", () -> BlockEntityType.Builder.of(
                    PlanetSimulatorBlockEntity::new,
                    MJBlocks.PLANET_SIMULATOR_CONTROLLER.get()
            ).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlanetSimulatorPartBlockEntity>> PLANET_SIMULATOR_PART =
            BLOCK_ENTITIES.register("planet_simulator_part", () -> BlockEntityType.Builder.of(
                    PlanetSimulatorPartBlockEntity::new,
                    MJBlocks.PLANET_SIMULATOR_PART.get()
            ).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlanetSimulatorBusBlockEntity>> PLANET_SIMULATOR_BUS =
            BLOCK_ENTITIES.register("planet_simulator_bus", () -> BlockEntityType.Builder.of(
                    PlanetSimulatorBusBlockEntity::new,
                    MJBlocks.PLANET_SIMULATOR_BUS.get()
            ).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CompressorBlockEntity>> COMPRESSOR =
            BLOCK_ENTITIES.register("compressor", () -> BlockEntityType.Builder.of(
                    CompressorBlockEntity::new,
                    MJBlocks.COMPRESSOR.get()
            ).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreativePowerBlockEntity>> CREATIVE_POWER =
            BLOCK_ENTITIES.register("creative_power", () -> BlockEntityType.Builder.of(
                    CreativePowerBlockEntity::new,
                    MJBlocks.CREATIVE_POWER.get()
            ).build(null));
}
