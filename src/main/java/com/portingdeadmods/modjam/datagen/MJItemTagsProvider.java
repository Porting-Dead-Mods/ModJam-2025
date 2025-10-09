package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MJItemTagsProvider extends ItemTagsProvider {

    public MJItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, Modjam.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.STORAGE_BLOCKS)
                .add(MJItems.TANTALUM_STORAGE_BLOCK.get());
        
        tag(Tags.Items.INGOTS)
                .add(MJItems.TANTALUM_INGOT.get());
        
        tag(Tags.Items.NUGGETS)
                .add(MJItems.TANTALUM_NUGGET.get());
        
        tag(Tags.Items.DUSTS)
                .add(MJItems.TANTALUM_DUST.get());
    }
}
