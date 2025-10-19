package com.portingdeadmods.spaceploitation.content.block.bus;

import com.portingdeadmods.spaceploitation.content.blockentity.bus.AbstractBusBlockEntity;
import com.portingdeadmods.spaceploitation.data.BusType;
import com.portingdeadmods.spaceploitation.registries.MJMultiblocks;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractBusBlock extends BaseEntityBlock {
    private final BusType busType;
    private final boolean isInput;

    protected AbstractBusBlock(Properties properties, BusType busType, boolean isInput) {
        super(properties);
        this.busType = busType;
        this.isInput = isInput;
        registerDefaultState(this.defaultBlockState()
                .setValue(Multiblock.FORMED, false));
    }

    public BusType getBusType() {
        return busType;
    }

    public boolean isInput() {
        return isInput;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(Multiblock.FORMED));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getValue(Multiblock.FORMED) && !state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof AbstractBusBlockEntity be) {
            BlockPos controllerPos = be.getControllerPos();
            if (controllerPos != null) {
                MultiblockHelper.unform(MJMultiblocks.PLANET_SIMULATOR.get(), controllerPos, level);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AbstractBusBlockEntity be) {
            if (state.getValue(Multiblock.FORMED)) {
                player.openMenu(be, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
