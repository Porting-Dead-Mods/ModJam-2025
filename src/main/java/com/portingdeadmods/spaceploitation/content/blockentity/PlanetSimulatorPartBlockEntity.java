package com.portingdeadmods.spaceploitation.content.blockentity;

import com.portingdeadmods.spaceploitation.registries.MJBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockPartEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PlanetSimulatorPartBlockEntity extends BlockEntity implements MultiblockPartEntity {
    private @Nullable BlockPos controllerPos;

    public PlanetSimulatorPartBlockEntity(BlockPos pos, BlockState blockState) {
        super(MJBlockEntities.PLANET_SIMULATOR_PART.get(), pos, blockState);
    }

    public PlanetSimulatorPartBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void commonTick() {
    }

    @Override
    public @Nullable BlockPos getControllerPos() {
        return this.controllerPos;
    }

    @Override
    public void setControllerPos(BlockPos blockPos) {
        this.controllerPos = blockPos;
    }

    @Override
    protected void loadAdditional(CompoundTag p_338466_, HolderLookup.Provider p_338445_) {
        super.loadAdditional(p_338466_, p_338445_);

        if (p_338466_.contains("controller_pos")) {
            this.controllerPos = BlockPos.of(p_338466_.getLong("controller_pos"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_, HolderLookup.Provider p_323635_) {
        super.saveAdditional(p_187471_, p_323635_);

        if (this.controllerPos != null) {
            p_187471_.putLong("controller_pos", this.controllerPos.asLong());
        }
    }
}
