package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.blockentity.CompressorBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBlockEntity;
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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CompressorBlockEntity>> COMPRESSOR =
            BLOCK_ENTITIES.register("compressor", () -> BlockEntityType.Builder.of(
                    CompressorBlockEntity::new,
                    MJBlocks.COMPRESSOR.get()
            ).build(null));
}
