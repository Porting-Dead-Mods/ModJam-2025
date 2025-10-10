package com.portingdeadmods.modjam.content.blockentity;

import com.portingdeadmods.modjam.registries.MJBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class PlanetSimulatorBusBlockEntity extends PlanetSimulatorPartBlockEntity {
    public PlanetSimulatorBusBlockEntity(BlockPos pos, BlockState blockState) {
        super(MJBlockEntities.PLANET_SIMULATOR_BUS.get(), pos, blockState);
    }
    public IEnergyStorage exposeEnergyStorage(Direction direction) {
        if (this.getControllerPos() != null) {
            return ((PlanetSimulatorBlockEntity) this.level.getBlockEntity(this.getControllerPos())).getEnergyStorage();
        }
        return null;
    }

    public IFluidHandler exposeFluidHandler(Direction direction) {
        if (this.getControllerPos() != null) {
            return ((PlanetSimulatorBlockEntity) this.level.getBlockEntity(this.getControllerPos())).getFluidHandler();
        }
        return null;
    }

    public IItemHandler exposeItemHandler(Direction direction) {
        if (this.getControllerPos() != null) {
            return ((PlanetSimulatorBlockEntity) this.level.getBlockEntity(this.getControllerPos())).getItemHandler();
        }
        return null;
    }

}
