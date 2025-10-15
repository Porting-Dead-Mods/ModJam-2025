package com.portingdeadmods.modjam.data;

import com.portingdeadmods.modjam.registries.MJItems;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public enum UpgradeType {
    ENERGY("energy", MJItems.ENERGY_UPGRADE, ModifierType.MULTIPLY, 2.0f),
    SPEED("speed", MJItems.SPEED_UPGRADE, ModifierType.MULTIPLY, 3.0f),
    LUCK("luck", MJItems.LUCK_UPGRADE, ModifierType.ADD, 1.25f);

    private final String name;
    private final Supplier<? extends Item> item;
    private final ModifierType modifierType;
    private final float modifierValue;

    UpgradeType(String name, Supplier<? extends Item> item, ModifierType modifierType, float modifierValue) {
        this.name = name;
        this.item = item;
        this.modifierType = modifierType;
        this.modifierValue = modifierValue;
    }

    public String getName() {
        return name;
    }

    public Item getItem() {
        return this.item.get();
    }

    public ModifierType getModifierType() {
        return modifierType;
    }

    public float getModifierValue() {
        return modifierValue;
    }

    public enum ModifierType {
        MULTIPLY,
        DIVIDE,
        ADD
    }

}
