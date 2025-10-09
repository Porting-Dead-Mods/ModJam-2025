package com.portingdeadmods.modjam.multiblock;

import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockDefinition;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TestMultiblock implements Multiblock {
    
    private static final MultiblockDefinition DEFINITION = new MultiblockDefinition();
    
    static {
        DEFINITION.put(0, Blocks.AIR);
        DEFINITION.put(1, Blocks.STONE_BRICKS);
        DEFINITION.put(2, Blocks.DIAMOND_BLOCK);
    }

    @Override
    public Block getUnformedController() {
        return MJBlocks.TEST_MULTIBLOCK_CONTROLLER.get();
    }

    @Override
    public Block getFormedController() {
        return MJBlocks.TEST_MULTIBLOCK_CONTROLLER_FORMED.get();
    }

    @Override
    public MultiblockLayer[] getLayout() {
        return new MultiblockLayer[] {
            layer(
                1, 1, 1,
                1, 2, 1,
                1, 1, 1
            ),
            layer(
                1, 1, 1,
                1, 0, 1,
                1, 1, 1
            ),
            layer(
                1, 1, 1,
                1, 2, 1,
                1, 1, 1
            )
        };
    }

    @Override
    public MultiblockDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    public BlockEntityType<? extends MultiblockEntity> getMultiBlockEntityType() {
        return MJBlockEntities.TEST_MULTIBLOCK_ENTITY.get();
    }

    @Override
    public @Nullable BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockData multiblockData, @Nullable Player player) {
        if (blockPos.equals(controllerPos)) {
            return getFormedController().defaultBlockState();
        }
        return null;
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos) {
        BlockState state = level.getBlockState(blockPos);
        return state.is(getFormedController());
    }
}
