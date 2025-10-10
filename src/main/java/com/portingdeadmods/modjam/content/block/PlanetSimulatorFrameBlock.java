package com.portingdeadmods.modjam.content.block;

import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class PlanetSimulatorFrameBlock extends Block {
    public PlanetSimulatorFrameBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(Multiblock.FORMED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(Multiblock.FORMED));
    }
}
