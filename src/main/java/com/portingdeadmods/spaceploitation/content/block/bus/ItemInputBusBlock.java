package com.portingdeadmods.spaceploitation.content.block.bus;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.spaceploitation.data.BusType;
import com.portingdeadmods.spaceploitation.registries.MJBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemInputBusBlock extends AbstractBusBlock {
    public ItemInputBusBlock(Properties properties) {
        super(properties, BusType.ITEM, true);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ItemInputBusBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return MJBlockEntities.ITEM_INPUT_BUS.get().create(pos, state);
    }
}
