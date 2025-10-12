package com.portingdeadmods.modjam.content.blockentity;

import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CreativePowerBlockEntity extends ContainerBlockEntity {
    public CreativePowerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MJBlockEntities.CREATIVE_POWER.get(), blockPos, blockState);
        addEnergyStorage(Integer.MAX_VALUE);
    }

    @Override
    public void commonTick() {
        if (level.isClientSide) return;
        
        getEnergyStorage().receiveEnergy(Integer.MAX_VALUE, false);
    }

    @Override
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> blockCapability) {
        if (blockCapability == Capabilities.EnergyStorage.BLOCK) {
            return Map.of(
                Direction.UP, Pair.of(IOAction.EXTRACT, new int[0]),
                Direction.DOWN, Pair.of(IOAction.EXTRACT, new int[0]),
                Direction.NORTH, Pair.of(IOAction.EXTRACT, new int[0]),
                Direction.SOUTH, Pair.of(IOAction.EXTRACT, new int[0]),
                Direction.EAST, Pair.of(IOAction.EXTRACT, new int[0]),
                Direction.WEST, Pair.of(IOAction.EXTRACT, new int[0])
            );
        }
        return Map.of();
    }
}
