package com.portingdeadmods.modjam.content.block.bus;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.modjam.content.blockentity.bus.EnergyOutputBusBlockEntity;
import com.portingdeadmods.modjam.data.BusType;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class EnergyOutputBusBlock extends AbstractBusBlock {
    public EnergyOutputBusBlock(Properties properties) {
        super(properties, BusType.ENERGY, false);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(EnergyOutputBusBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MJBlockEntities.ENERGY_OUTPUT_BUS.get().create(pos, state);
    }

}
