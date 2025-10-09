package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.block.PlanetSimulatorBusBlock;
import com.portingdeadmods.modjam.content.block.PlanetSimulatorControllerBlock;
import com.portingdeadmods.modjam.content.block.PlanetSimulatorPartBlock;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

public final class MJBlocks {
    public static final PDLDeferredRegisterBlocks BLOCKS = PDLDeferredRegisterBlocks.createBlocksRegister(Modjam.MODID, MJItems.ITEMS);
    
    public static final BlockBehaviour.Properties TANTALUM_PROPERTIES = BlockBehaviour.Properties.of().strength(5.0f, 6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL);
    
    public static final DeferredBlock<PlanetSimulatorControllerBlock> PLANET_SIMULATOR_CONTROLLER = BLOCKS.registerBlockWithItem("planet_simulator_controller", PlanetSimulatorControllerBlock::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<PlanetSimulatorBusBlock> PLANET_SIMULATOR_BUS = BLOCKS.registerBlockWithItem("planet_simulator_bus", PlanetSimulatorBusBlock::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<PlanetSimulatorPartBlock> PLANET_SIMULATOR_PART = BLOCKS.registerBlockWithItem("planet_simulator_part", PlanetSimulatorPartBlock::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<Block> TANTALUM_STORAGE_BLOCK = BLOCKS.registerBlockWithItem("tantalum_storage_block", Block::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<Block> PLANET_SIMULATOR_CASING = BLOCKS.registerBlockWithItem("planet_simulator_casing", Block::new, TANTALUM_PROPERTIES);

    public static final DeferredBlock<Block> TANTALUM_ORE = BLOCKS.registerWithItem("tantalum_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_TANTALUM_ORE = BLOCKS.registerWithItem("deepslate_tantalum_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

}
