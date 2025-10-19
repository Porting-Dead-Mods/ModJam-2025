package com.portingdeadmods.spaceploitation.content.blockentity.bus;

import com.portingdeadmods.spaceploitation.content.blockentity.PlanetSimulatorPartBlockEntity;
import com.portingdeadmods.spaceploitation.data.BusType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractBusBlockEntity extends PlanetSimulatorPartBlockEntity implements MenuProvider {
    private final BusType busType;
    private final boolean isInput;

    protected AbstractBusBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, BusType busType, boolean isInput) {
        super(type, pos, blockState);
        this.busType = busType;
        this.isInput = isInput;
    }

    public BusType getBusType() {
        return busType;
    }

    public boolean isInput() {
        return isInput;
    }
}
