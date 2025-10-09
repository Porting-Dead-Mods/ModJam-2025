package com.portingdeadmods.modjam.content.multiblock;

import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockDefinition;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlanetSimulatorMultiblock implements Multiblock {
    @Override
    public Block getUnformedController() {
        return MJBlocks.PLANET_SIMULATOR_CONTROLLER.get();
    }

    @Override
    public Block getFormedController() {
        return MJBlocks.PLANET_SIMULATOR_CONTROLLER.get();
    }

    @Override
    public MultiblockLayer[] getLayout() {
        return new MultiblockLayer[]{
                layer(
                        2, 0, 0, 0, 0, 0, 2,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        2, 0, 0, 0, 0, 0, 2
                ),
                layer(
                        2, 0, 0, 0, 0, 0, 2,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        3, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        2, 0, 0, 0, 0, 0, 2
                ),
                layer(
                        1, 1, 1, 1, 1, 1, 1,
                        1, 2, 2, 2, 2, 2, 1,
                        1, 2, 0, 0, 0, 2, 1,
                        1, 2, 0, 0, 0, 2, 1,
                        1, 2, 0, 0, 0, 2, 1,
                        1, 2, 2, 2, 2, 2, 1,
                        1, 1, 1, 1, 1, 1, 1
                ),
        };
    }

    @Override
    public MultiblockDefinition getDefinition() {
        MultiblockDefinition def = new MultiblockDefinition();
        def.put(0, state -> state.is(MJBlocks.PLANET_SIMULATOR_CASING.get()) || state.is(MJBlocks.PLANET_SIMULATOR_BUS), MJBlocks.PLANET_SIMULATOR_CASING.get());
        def.put(1, BlockState::isEmpty, Blocks.AIR);
        def.put(2, MJBlocks.TANTALUM_STORAGE_BLOCK.get());
        def.put(3, this.getUnformedController());
        return def;
    }

    @Override
    public BlockEntityType<? extends MultiblockEntity> getMultiBlockEntityType() {
        return MJBlockEntities.PLANET_SIMULATOR.get();
    }

    @Override
    public @Nullable BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockData multiblockData, @Nullable Player player) {
        int id = this.getLayout()[layoutIndex].layer()[layerIndex];
        BlockState state = level.getBlockState(blockPos);
        if (id != 1) {
            return state.setValue(Multiblock.FORMED, true);
        }
        return state;
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos) {
        return false;
    }
}
