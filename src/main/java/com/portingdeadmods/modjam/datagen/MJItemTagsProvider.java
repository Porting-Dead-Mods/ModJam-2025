package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.modjam.registries.MJItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
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
                .add(MJBlocks.TANTALUM_STORAGE_BLOCK.asItem());
        
        tag(Tags.Items.INGOTS)
                .add(MJItems.TANTALUM_INGOT.asItem());
        
        tag(Tags.Items.NUGGETS)
                .add(MJItems.TANTALUM_NUGGET.asItem());
        
        tag(Tags.Items.ORES)
                .add(MJBlocks.TANTALUM_ORE.asItem())
                .add(MJBlocks.DEEPSLATE_TANTALUM_ORE.asItem());
        
        tag(Tags.Items.RAW_MATERIALS)
                .add(MJItems.RAW_TANTALUM.get());
        
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/tantalum")))
                .add(MJBlocks.TANTALUM_STORAGE_BLOCK.asItem());
        
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ores/tantalum")))
                .add(MJBlocks.TANTALUM_ORE.asItem())
                .add(MJBlocks.DEEPSLATE_TANTALUM_ORE.asItem());
        
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/tantalum")))
                .add(MJItems.TANTALUM_INGOT.get());
        
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "nuggets/tantalum")))
                .add(MJItems.TANTALUM_NUGGET.get());
        
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "raw_materials/tantalum")))
                .add(MJItems.RAW_TANTALUM.get());

    }
}
