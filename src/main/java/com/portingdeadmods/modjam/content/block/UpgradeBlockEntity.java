package com.portingdeadmods.modjam.content.block;

import com.portingdeadmods.modjam.capabilities.UpgradeItemHandler;
import com.portingdeadmods.modjam.data.UpgradeType;
import net.minecraft.resources.ResourceKey;

import java.util.Set;

public interface UpgradeBlockEntity {
    UpgradeItemHandler getUpgradeItemHandler();

    Set<ResourceKey<UpgradeType>> getSupportedUpgrades();

    boolean hasUpgrade(ResourceKey<UpgradeType> upgrade);

    int getUpgradeAmount(ResourceKey<UpgradeType> upgrade);

    void onUpgradeAdded(ResourceKey<UpgradeType> upgrade);

    void onUpgradeRemoved(ResourceKey<UpgradeType> upgrade);

}