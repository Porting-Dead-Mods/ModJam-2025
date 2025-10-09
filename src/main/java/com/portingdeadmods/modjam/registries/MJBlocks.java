package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.block.CompressorBlock;
import com.portingdeadmods.modjam.content.block.PlanetSimulatorControllerBlock;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

public final class MJBlocks {
    public static final PDLDeferredRegisterBlocks BLOCKS = PDLDeferredRegisterBlocks.createBlocksRegister(Modjam.MODID, MJItems.ITEMS);
    
    public static final BlockBehaviour.Properties TANTALUM_PROPERTIES = BlockBehaviour.Properties.of().strength(5.0f, 6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL);
    
    public static final DeferredBlock<PlanetSimulatorControllerBlock> PLANET_SIMULATOR_CONTROLLER = BLOCKS.registerBlockWithItem("planet_simulator_controller", PlanetSimulatorControllerBlock::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<Block> TANTALUM_STORAGE_BLOCK = BLOCKS.registerBlockWithItem("tantalum_storage_block", Block::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<Block> PLANET_SIMULATOR_CASING = BLOCKS.registerBlockWithItem("planet_simulator_casing", Block::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<Block> TANTALUM_ORE = BLOCKS.registerWithItem("tantalum_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_TANTALUM_ORE = BLOCKS.registerWithItem("deepslate_tantalum_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<CompressorBlock> COMPRESSOR = BLOCKS.registerBlockWithItem("compressor", CompressorBlock::new, TANTALUM_PROPERTIES);
}
