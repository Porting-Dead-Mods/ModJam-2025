package com.portingdeadmods.modjam.content.blockentity;

import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlanetSimulatorBlockEntity extends ContainerBlockEntity implements MultiblockEntity {
    private MultiblockData multiblockData;

    public PlanetSimulatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MJBlockEntities.PLANET_SIMULATOR.get(), blockPos, blockState);
    }

    @Override
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> blockCapability) {
        return Map.of();
    }

    @Override
    public MultiblockData getMultiblockData() {
        return this.multiblockData;
    }

    @Override
    public void setMultiblockData(MultiblockData multiblockData) {
        this.multiblockData = multiblockData;
        this.setChanged();
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("multiblock_data", this.saveMBData());
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("multiblock_data")) {
            this.multiblockData = this.loadMBData(tag.getCompound("multiblock_data"));
        }
    }

}
