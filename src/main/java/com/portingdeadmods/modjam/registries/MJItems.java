package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.items.PlanetCardItem;
import com.portingdeadmods.modjam.content.items.TintedPlanetCardItem;
import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLDeferredRegisterItems;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public final class MJItems {
    public static final PDLDeferredRegisterItems ITEMS = PDLDeferredRegisterItems.createItemsRegister(Modjam.MODID);

    public static final DeferredItem<Item> RAW_TANTALUM = ITEMS.registerSimpleItem("raw_tantalum");

    public static final DeferredItem<Item> TANTALUM_INGOT = ITEMS.registerSimpleItem("tantalum_ingot");
    
    public static final DeferredItem<Item> TANTALUM_NUGGET = ITEMS.registerSimpleItem("tantalum_nugget");
    
    public static final DeferredItem<Item> TANTALUM_SHEET = ITEMS.registerSimpleItem("tantalum_sheet");

    public static final DeferredItem<PlanetCardItem> PLANET_CARD = ITEMS.register("planet_card", () -> new PlanetCardItem(new Item.Properties()
            .component(MJDataComponents.PLANET, PlanetComponent.EMPTY)));

    public static final DeferredItem<TintedPlanetCardItem> TINTED_PLANET_CARD = ITEMS.register("tinted_planet_card", () -> new TintedPlanetCardItem(new Item.Properties()
            .component(MJDataComponents.PLANET, PlanetComponent.EMPTY)));

}
