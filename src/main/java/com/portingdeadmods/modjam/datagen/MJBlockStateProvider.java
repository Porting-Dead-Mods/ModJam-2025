package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.block.PlanetSimulatorControllerBlock;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.portingdeadlibs.api.datagen.ModelBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

public class MJBlockStateProvider extends BlockStateProvider {

    public MJBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Modjam.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(MJBlocks.TANTALUM_STORAGE_BLOCK.get(),
            cubeAll(MJBlocks.TANTALUM_STORAGE_BLOCK.get()));
        simpleBlockWithItem(MJBlocks.TANTALUM_ORE.get(), 
            cubeAll(MJBlocks.TANTALUM_ORE.get()));
        simpleBlockWithItem(MJBlocks.DEEPSLATE_TANTALUM_ORE.get(), 
            cubeAll(MJBlocks.DEEPSLATE_TANTALUM_ORE.get()));
        simpleBlockWithItem(MJBlocks.PLANET_SIMULATOR_CASING.get(),
                cubeAll(MJBlocks.PLANET_SIMULATOR_CASING.get()));

        builder(MJBlocks.PLANET_SIMULATOR_CONTROLLER)
                .defaultTexture(this.blockTexture(MJBlocks.PLANET_SIMULATOR_CASING.get()))
                .front(this::blockTexture)
                .horizontalFacing()
                .create();

        builder(MJBlocks.PLANET_SIMULATOR_BUS)
                .defaultTexture(this.blockTexture(MJBlocks.PLANET_SIMULATOR_CASING.get()))
                .sides(this::blockTexture)
                .create();

        builder(MJBlocks.COMPRESSOR)
                .defaultTexture(blockTexture(MJBlocks.TANTALUM_STORAGE_BLOCK.get()))
                .front((provider, suffix) -> blockTexture(MJBlocks.COMPRESSOR.get()), "_front")
                .horizontalFacing()
                .active()
                .create();

    }

    private @NotNull ModelBuilder builder(DeferredBlock<? extends Block> block) {
        return new ModelBuilder(block.get(), this);
    }
}
