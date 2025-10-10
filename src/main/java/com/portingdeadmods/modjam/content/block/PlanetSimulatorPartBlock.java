package com.portingdeadmods.modjam.content.block;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class PlanetSimulatorPartBlock extends BaseEntityBlock {
    // FIXME: Ctm works even when one block is frame and other is casing
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return MJBlocks.PLANET_SIMULATOR_CASING.toStack();
    }

    public enum Variant implements StringRepresentable {
        CASING("casing"),
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
