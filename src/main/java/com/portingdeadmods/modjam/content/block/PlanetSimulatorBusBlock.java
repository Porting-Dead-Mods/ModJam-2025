package com.portingdeadmods.modjam.content.block;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBusBlockEntity;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorPartBlockEntity;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.modjam.registries.MJMultiblocks;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class PlanetSimulatorBusBlock extends BaseEntityBlock {
    public PlanetSimulatorBusBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState()
                .setValue(Multiblock.FORMED, false));
    }

    @Override
    protected RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(PlanetSimulatorBusBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return MJBlockEntities.PLANET_SIMULATOR_BUS.get().create(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(Multiblock.FORMED));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getValue(Multiblock.FORMED) && !state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof PlanetSimulatorBusBlockEntity be) {
            BlockPos controllerPos = be.getControllerPos();
            if (controllerPos != null) {
                MultiblockHelper.unform(MJMultiblocks.PLANET_SIMULATOR.get(), controllerPos, level);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);

    }

}
