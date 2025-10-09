package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Modjam.MODID);

    public static final DeferredItem<BlockItem> TEST_MULTIBLOCK_CONTROLLER = ITEMS.register("test_multiblock_controller",
            () -> new BlockItem(MJBlocks.TEST_MULTIBLOCK_CONTROLLER.get(), new Item.Properties()));
    
    public static final DeferredItem<BlockItem> TEST_MULTIBLOCK_CONTROLLER_FORMED = ITEMS.register("test_multiblock_controller_formed",
            () -> new BlockItem(MJBlocks.TEST_MULTIBLOCK_CONTROLLER_FORMED.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> TANTALUM_STORAGE_BLOCK = ITEMS.register("tantalum_storage_block",
            () -> new BlockItem(MJBlocks.TANTALUM_STORAGE_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<Item> TANTALUM_INGOT = ITEMS.register("tantalum_ingot",
            () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> TANTALUM_NUGGET = ITEMS.register("tantalum_nugget",
            () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> TANTALUM_DUST = ITEMS.register("tantalum_dust",
            () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> TANTALUM_SHEET = ITEMS.register("tantalum_sheet",
            () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> TANTALUM_SEMI_PRESSED_SHEET = ITEMS.register("tantalum_semi_pressed_sheet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PESTLE = ITEMS.register("pestle",
            () -> new Item(new Item.Properties().durability(32)));

    public static final DeferredItem<Item> MORTAR = ITEMS.register("mortar",
            () -> new Item(new Item.Properties().durability(32)));

    public static final DeferredItem<Item> HAMMER = ITEMS.register("hammer",
            () -> new Item(new Item.Properties().durability(128)));

    public static final DeferredItem<Item> RAW_TANTALUM = ITEMS.register("raw_tantalum",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<BlockItem> TANTALUM_ORE = ITEMS.register("tantalum_ore",
            () -> new BlockItem(MJBlocks.TANTALUM_ORE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> DEEPSLATE_TANTALUM_ORE = ITEMS.register("deepslate_tantalum_ore",
            () -> new BlockItem(MJBlocks.DEEPSLATE_TANTALUM_ORE.get(), new Item.Properties()));
}
