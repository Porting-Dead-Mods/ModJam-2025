package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.block.*;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterBlocks;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Set;

public final class MJBlocks {
    public static final PDLDeferredRegisterBlocks BLOCKS = PDLDeferredRegisterBlocks.createBlocksRegister(Modjam.MODID, MJItems.ITEMS);

    public static final BlockBehaviour.Properties TANTALUM_PROPERTIES = BlockBehaviour.Properties.of().strength(5.0f, 6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL);

    public static final DeferredBlock<PlanetSimulatorControllerBlock> PLANET_SIMULATOR_CONTROLLER = BLOCKS.registerBlockWithItem("planet_simulator_controller", PlanetSimulatorControllerBlock::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<PlanetSimulatorBusBlock> PLANET_SIMULATOR_BUS = BLOCKS.registerBlockWithItem("planet_simulator_bus", PlanetSimulatorBusBlock::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<PlanetSimulatorPartBlock> PLANET_SIMULATOR_PART = BLOCKS.registerBlock("planet_simulator_part", PlanetSimulatorPartBlock::new, TANTALUM_PROPERTIES);
    public static final DeferredBlock<Block> TANTALUM_STORAGE_BLOCK = BLOCKS.registerBlockWithItem("tantalum_storage_block", Block::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<Block> PLANET_SIMULATOR_CASING = BLOCKS.registerBlockWithItem("planet_simulator_casing", Block::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<PlanetSimulatorFrameBlock> PLANET_SIMULATOR_FRAME = BLOCKS.registerBlockWithItem("planet_simulator_frame", PlanetSimulatorFrameBlock::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<Block> TANTALUM_ORE = BLOCKS.registerWithItem("tantalum_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_TANTALUM_ORE = BLOCKS.registerWithItem("deepslate_tantalum_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<CompressorBlock> COMPRESSOR = BLOCKS.registerBlockWithItem("compressor", CompressorBlock::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<CompressorGhostBlock> COMPRESSOR_GHOST = BLOCKS.registerBlock("compressor_ghost", CompressorGhostBlock::new, BlockBehaviour.Properties.of().noCollission().noOcclusion().replaceable());

    public static final DeferredBlock<CreativePowerBlock> CREATIVE_POWER = BLOCKS.registerBlockWithItem("creative_power", CreativePowerBlock::new, TANTALUM_PROPERTIES);

    public static final Set<DeferredBlock<?>> NO_ITEM_MODELS = Set.of(MJBlocks.COMPRESSOR, MJBlocks.CREATIVE_POWER, MJBlocks.TANTALUM_ORE, MJBlocks.DEEPSLATE_TANTALUM_ORE);
}
