package com.portingdeadmods.modjam.content.block;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.modjam.registries.MJMultiblocks;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableContainerBlock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class PlanetSimulatorControllerBlock extends RotatableContainerBlock {
    public PlanetSimulatorControllerBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState()
                .setValue(Multiblock.FORMED, false));
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return MJBlockEntities.PLANET_SIMULATOR.get();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(PlanetSimulatorControllerBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(Multiblock.FORMED));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && state.getValue(Multiblock.FORMED)) {
            MultiblockHelper.unform(MJMultiblocks.PLANET_SIMULATOR.get(), pos, level);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);

    }
}
