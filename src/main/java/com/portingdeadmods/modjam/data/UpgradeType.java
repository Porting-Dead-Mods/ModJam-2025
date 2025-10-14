package com.portingdeadmods.modjam.data;

import com.portingdeadmods.modjam.registries.MJItems;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public enum UpgradeType {
    ENERGY("energy", MJItems.ENERGY_UPGRADE),
    SPEED("speed", MJItems.SPEED_UPGRADE),
    LUCK("luck", MJItems.LUCK_UPGRADE);

    private final String name;
    private final Supplier<? extends Item> item;

    UpgradeType(String name, Supplier<? extends Item> item) {
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public Item getItem() {
        return this.item.get();
    }

}
