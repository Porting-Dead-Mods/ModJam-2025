package com.portingdeadmods.spaceploitation.content.block;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.spaceploitation.content.blockentity.PlanetSimulatorBlockEntity;
import com.portingdeadmods.spaceploitation.registries.MJBlockEntities;
import com.portingdeadmods.spaceploitation.registries.MJMultiblocks;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.FakeBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableContainerBlock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

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
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, BlockHitResult p_60508_) {
        BlockEntity blockEntity = p_60504_.getBlockEntity(p_60505_);
        if (blockEntity instanceof MenuProvider menuProvider) {
            BlockPos pos = p_60505_;
            if (blockEntity instanceof FakeBlockEntity fakeBlockEntity && fakeBlockEntity.getActualBlockEntityPos() != null) {
                pos = fakeBlockEntity.getActualBlockEntityPos();
            }
            if (p_60503_.getValue(Multiblock.FORMED)) {
                p_60506_.openMenu(menuProvider, pos);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(p_60503_, p_60504_, p_60505_, p_60506_, p_60508_);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PlanetSimulatorBlockEntity planetSimulator) {
                for (int i = 0; i < planetSimulator.getUpgradeItemHandler().getSlots(); i++) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), 
                            planetSimulator.getUpgradeItemHandler().getStackInSlot(i));
                }
            }
            
            if (state.getValue(Multiblock.FORMED)) {
                MultiblockHelper.unform(MJMultiblocks.PLANET_SIMULATOR.get(), pos, level);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);

    }
}
