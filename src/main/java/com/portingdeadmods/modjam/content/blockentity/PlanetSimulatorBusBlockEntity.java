package com.portingdeadmods.modjam.content.blockentity;

import com.portingdeadmods.modjam.registries.MJBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PlanetSimulatorBusBlockEntity extends PlanetSimulatorPartBlockEntity {
    public PlanetSimulatorBusBlockEntity(BlockPos pos, BlockState blockState) {
        super(MJBlockEntities.PLANET_SIMULATOR_BUS.get(), pos, blockState);
    }
}
