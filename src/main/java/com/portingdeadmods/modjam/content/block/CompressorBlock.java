package com.portingdeadmods.modjam.content.block;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableContainerBlock;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class CompressorBlock extends RotatableContainerBlock {
    public CompressorBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState()
                .setValue(PDLBlockStateProperties.ACTIVE, false));
    }

    private static final VoxelShape SHAPE_NORTH = makeShape();
    private static final VoxelShape SHAPE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST = rotateShape(Direction.NORTH, Direction.WEST, SHAPE_NORTH);
    private static final VoxelShape SHAPE_EAST = rotateShape(Direction.NORTH, Direction.EAST, SHAPE_NORTH);

    private static VoxelShape makeShape() {
        return Stream.of(
                Block.box(1, 0, 1, 15, 2, 15),
                Block.box(11, 10, 1, 15, 12, 15),
                Block.box(1, 10, 1, 5, 12, 15),
                Block.box(5, 10, 11, 11, 12, 15),
                Block.box(5, 10, 1, 11, 12, 5),
                Block.box(13, 0, 6, 16, 16, 10),
                Block.box(13, 16, 6, 16, 26, 10),
                Block.box(3, 20, 5, 13, 23, 11),
                Block.box(0, 16, 6, 3, 26, 10),
                Block.box(0, 0, 6, 3, 16, 10),
                Block.box(2, 2, 10, 14, 10, 14),
                Block.box(2, 2, 2, 14, 10, 6)
        ).reduce(Shapes::or).get();
    }

    private static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1],
                    Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(PDLBlockStateProperties.ACTIVE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return MJBlockEntities.COMPRESSOR.get();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CompressorBlock::new);
    }
}
