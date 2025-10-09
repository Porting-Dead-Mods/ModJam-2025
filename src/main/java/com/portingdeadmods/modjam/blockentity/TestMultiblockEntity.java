package com.portingdeadmods.modjam.blockentity;

import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TestMultiblockEntity extends BlockEntity implements MultiblockEntity {
    
    private MultiblockData multiblockData = MultiblockData.EMPTY;

    public TestMultiblockEntity(BlockPos pos, BlockState blockState) {
        super(MJBlockEntities.TEST_MULTIBLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    public MultiblockData getMultiblockData() {
        return multiblockData;
    }

    @Override
    public void setMultiblockData(MultiblockData data) {
        this.multiblockData = data;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("MultiblockData", saveMBData());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("MultiblockData")) {
            this.multiblockData = loadMBData(tag.getCompound("MultiblockData"));
        }
    }
}
