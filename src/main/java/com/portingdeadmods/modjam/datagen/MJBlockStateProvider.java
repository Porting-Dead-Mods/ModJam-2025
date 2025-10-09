package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MJBlockStateProvider extends BlockStateProvider {

    public MJBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Modjam.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(MJBlocks.TANTALUM_STORAGE_BLOCK.get(), 
            cubeAll(MJBlocks.TANTALUM_STORAGE_BLOCK.get()));
    }
}
