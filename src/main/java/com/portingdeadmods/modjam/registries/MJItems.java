package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterBlocks;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJItems {
    public static final PDLDeferredRegisterItems ITEMS = PDLDeferredRegisterItems.createItemsRegister(Modjam.MODID);

    public static final DeferredItem<Item> RAW_TANTALUM = ITEMS.register("raw_tantalum",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TANTALUM_INGOT = ITEMS.register("tantalum_ingot",
            () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> TANTALUM_NUGGET = ITEMS.register("tantalum_nugget",
            () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> TANTALUM_SHEET = ITEMS.register("tantalum_sheet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PESTLE = ITEMS.register("pestle",
            () -> new Item(new Item.Properties().durability(32)));

    public static final DeferredItem<Item> MORTAR = ITEMS.register("mortar",
            () -> new Item(new Item.Properties().durability(32)));

    public static final DeferredItem<Item> HAMMER = ITEMS.register("hammer",
            () -> new Item(new Item.Properties().durability(128)));

}
