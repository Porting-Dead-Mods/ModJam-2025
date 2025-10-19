package com.portingdeadmods.spaceploitation.content.block.bus;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.spaceploitation.data.BusType;
import com.portingdeadmods.spaceploitation.registries.MJBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
