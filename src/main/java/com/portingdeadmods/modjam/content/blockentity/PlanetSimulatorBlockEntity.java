package com.portingdeadmods.modjam.content.blockentity;

import com.portingdeadmods.modjam.capabilities.ReadOnlyEnergyStorage;
import com.portingdeadmods.modjam.capabilities.ReadOnlyFluidHandler;
import com.portingdeadmods.modjam.capabilities.ReadOnlyItemHandler;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.modjam.registries.MJMultiblocks;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
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
        this.addEnergyStorage(10_000_000);
        this.addItemHandler(9);
        this.addFluidTank(16_000);
    }

    @Override
    public void commonTick() {
        super.commonTick();

        if (this.level.getGameTime() % 20 == 0 && this.getBlockState().getValue(Multiblock.FORMED)) {
            MJMultiblocks.PLANET_SIMULATOR.get().form(this.level, this.worldPosition);
        }

    }

    public ReadOnlyEnergyStorage getEnergyStorageReadOnly(Direction direction) {
        return new ReadOnlyEnergyStorage(this.getEnergyStorage());
    }

    public ReadOnlyItemHandler getItemHandlerReadOnly(Direction direction) {
        return new ReadOnlyItemHandler(this.getItemHandler());
    }

    public ReadOnlyFluidHandler getFluidHandlerReadOnly(Direction direction) {
        return new ReadOnlyFluidHandler(this.getFluidHandler());
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
        if (this.getMultiblockData() != null) {
            tag.put("multiblock_data", this.saveMBData());
        }
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("multiblock_data")) {
            this.multiblockData = this.loadMBData(tag.getCompound("multiblock_data"));
        }
    }

}
