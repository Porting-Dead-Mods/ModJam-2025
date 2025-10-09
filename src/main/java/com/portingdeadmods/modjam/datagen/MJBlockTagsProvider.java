package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MJBlockTagsProvider extends BlockTagsProvider {

    public MJBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Modjam.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MJBlocks.TANTALUM_STORAGE_BLOCK.get());
        
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(MJBlocks.TANTALUM_STORAGE_BLOCK.get());
        
        tag(Tags.Blocks.STORAGE_BLOCKS)
                .add(MJBlocks.TANTALUM_STORAGE_BLOCK.get());
    }
}
