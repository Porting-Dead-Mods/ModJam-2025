package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Modjam.MODID);

    public static final DeferredBlock<Block> TEST_MULTIBLOCK_CONTROLLER = BLOCKS.register("test_multiblock_controller",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0f).requiresCorrectToolForDrops()));
    
    public static final DeferredBlock<Block> TEST_MULTIBLOCK_CONTROLLER_FORMED = BLOCKS.register("test_multiblock_controller_formed",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0f).requiresCorrectToolForDrops().noLootTable()));

    public static final DeferredBlock<Block> TANTALUM_STORAGE_BLOCK = BLOCKS.register("tantalum_storage_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5.0f, 6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> TANTALUM_ORE = BLOCKS.register("tantalum_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_TANTALUM_ORE = BLOCKS.register("deepslate_tantalum_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));
}
