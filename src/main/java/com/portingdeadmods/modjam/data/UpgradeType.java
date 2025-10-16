package com.portingdeadmods.modjam.data;

import com.portingdeadmods.modjam.registries.MJItems;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public enum UpgradeType {
    SPEED("speed", MJItems.SPEED_UPGRADE, 
        new UpgradeEffect(EffectTarget.DURATION, -10.0f),
        new UpgradeEffect(EffectTarget.ENERGY_USAGE, 5.0f)),
    
    ENERGY("energy", MJItems.ENERGY_UPGRADE, 
        new UpgradeEffect(EffectTarget.ENERGY_USAGE, -10.0f)),
    
    LUCK("luck", MJItems.LUCK_UPGRADE, 
        new UpgradeEffect(EffectTarget.LUCK_CHANCE, 20.0f),
        new UpgradeEffect(EffectTarget.ENERGY_USAGE, 5.0f));

    private final String name;
    private final Supplier<? extends Item> item;
    private final UpgradeEffect[] effects;

    UpgradeType(String name, Supplier<? extends Item> item, UpgradeEffect... effects) {
        this.name = name;
        this.item = item;
        this.effects = effects;
    }

    public String getName() {
        return name;
    }

    public Item getItem() {
        return this.item.get();
    }

    public UpgradeEffect[] getEffects() {
        return effects;
    }

    public static class UpgradeEffect {
        private final EffectTarget target;
        private final float percentPerUpgrade;

        public UpgradeEffect(EffectTarget target, float percentPerUpgrade) {
            this.target = target;
            this.percentPerUpgrade = percentPerUpgrade;
        }

        public EffectTarget getTarget() {
            return target;
        }

        public float getPercentPerUpgrade() {
            return percentPerUpgrade;
        }
        
        public float apply(float baseValue, int upgradeCount) {
            if (upgradeCount == 0) return baseValue;
            float totalPercent = percentPerUpgrade * upgradeCount;
            return baseValue * (1.0f + totalPercent / 100.0f);
        }
    }

    public enum EffectTarget {
        DURATION,
        ENERGY_USAGE,
        LUCK_CHANCE
    }
}
