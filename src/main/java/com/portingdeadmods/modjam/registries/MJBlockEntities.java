package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.blockentity.TestMultiblockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Modjam.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TestMultiblockEntity>> TEST_MULTIBLOCK_ENTITY =
            BLOCK_ENTITIES.register("test_multiblock_entity", () -> BlockEntityType.Builder.of(
                    TestMultiblockEntity::new,
                    MJBlocks.TEST_MULTIBLOCK_CONTROLLER.get(),
                    MJBlocks.TEST_MULTIBLOCK_CONTROLLER_FORMED.get()
            ).build(null));
}
