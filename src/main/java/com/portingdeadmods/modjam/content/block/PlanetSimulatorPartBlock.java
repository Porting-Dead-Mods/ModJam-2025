package com.portingdeadmods.modjam.content.block;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorPartBlockEntity;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.modjam.registries.MJMultiblocks;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlanetSimulatorPartBlock extends BaseEntityBlock {
    public static final EnumProperty<Variant> VARIANT = EnumProperty.create("variant", Variant.class);

    public PlanetSimulatorPartBlock(Properties properties) {
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
        return simpleCodec(PlanetSimulatorPartBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return MJBlockEntities.PLANET_SIMULATOR_PART.get().create(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(Multiblock.FORMED, VARIANT));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return switch (state.getValue(VARIANT)) {
            case CASING, PROJECTOR -> MJBlocks.PLANET_SIMULATOR_CASING.toStack();
            case FRAME -> MJBlocks.PLANET_SIMULATOR_FRAME.toStack();
        };
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getValue(Multiblock.FORMED) && !state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof PlanetSimulatorPartBlockEntity be) {
            BlockPos controllerPos = be.getControllerPos();
            if (controllerPos != null) {
                MultiblockHelper.unform(MJMultiblocks.PLANET_SIMULATOR.get(), controllerPos, level);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);

    }

    public enum Variant implements StringRepresentable {
        CASING("casing"),
        PROJECTOR("projector"),
        FRAME("frame");

        private final String name;

        Variant(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

}
