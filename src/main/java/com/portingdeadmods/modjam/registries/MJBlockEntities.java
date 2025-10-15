package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.blockentity.CompressorBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.CreativePowerBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorPartBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.EnergyInputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.EnergyOutputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.FluidInputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.FluidOutputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.ItemInputBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.bus.ItemOutputBusBlockEntity;
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
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyInputBusBlockEntity>> ENERGY_INPUT_BUS =
            BLOCK_ENTITIES.register("energy_input_bus", () -> BlockEntityType.Builder.of(
                    EnergyInputBusBlockEntity::new,
                    MJBlocks.ENERGY_INPUT_BUS.get()
            ).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyOutputBusBlockEntity>> ENERGY_OUTPUT_BUS =
            BLOCK_ENTITIES.register("energy_output_bus", () -> BlockEntityType.Builder.of(
                    EnergyOutputBusBlockEntity::new,
                    MJBlocks.ENERGY_OUTPUT_BUS.get()
            ).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ItemInputBusBlockEntity>> ITEM_INPUT_BUS =
            BLOCK_ENTITIES.register("item_input_bus", () -> BlockEntityType.Builder.of(
                    ItemInputBusBlockEntity::new,
                    MJBlocks.ITEM_INPUT_BUS.get()
            ).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ItemOutputBusBlockEntity>> ITEM_OUTPUT_BUS =
            BLOCK_ENTITIES.register("item_output_bus", () -> BlockEntityType.Builder.of(
                    ItemOutputBusBlockEntity::new,
                    MJBlocks.ITEM_OUTPUT_BUS.get()
            ).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidInputBusBlockEntity>> FLUID_INPUT_BUS =
            BLOCK_ENTITIES.register("fluid_input_bus", () -> BlockEntityType.Builder.of(
                    FluidInputBusBlockEntity::new,
                    MJBlocks.FLUID_INPUT_BUS.get()
            ).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidOutputBusBlockEntity>> FLUID_OUTPUT_BUS =
            BLOCK_ENTITIES.register("fluid_output_bus", () -> BlockEntityType.Builder.of(
                    FluidOutputBusBlockEntity::new,
                    MJBlocks.FLUID_OUTPUT_BUS.get()
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
