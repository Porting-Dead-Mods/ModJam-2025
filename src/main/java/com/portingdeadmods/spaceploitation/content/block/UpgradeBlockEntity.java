package com.portingdeadmods.spaceploitation.content.block;

import com.portingdeadmods.spaceploitation.capabilities.UpgradeItemHandler;
import com.portingdeadmods.spaceploitation.data.UpgradeType;
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