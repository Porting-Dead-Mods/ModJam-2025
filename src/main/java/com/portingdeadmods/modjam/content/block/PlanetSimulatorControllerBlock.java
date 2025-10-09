package com.portingdeadmods.modjam.content.block;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableContainerBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PlanetSimulatorControllerBlock extends RotatableContainerBlock {
    public PlanetSimulatorControllerBlock(Properties properties) {
        super(properties);
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
}
