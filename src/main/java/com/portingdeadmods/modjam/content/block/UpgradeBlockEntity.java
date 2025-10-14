package com.portingdeadmods.modjam.content.block;

import com.portingdeadmods.modjam.capabilities.UpgradeItemHandler;
import com.portingdeadmods.modjam.data.UpgradeType;

import java.util.Set;

public interface UpgradeBlockEntity {
    UpgradeItemHandler getUpgradeItemHandler();

    Set<UpgradeType> getSupportedUpgrades();

    boolean hasUpgrade(UpgradeType upgrade);

    int getUpgradeAmount(UpgradeType upgrade);

    void onUpgradeAdded(UpgradeType upgrade);

    void onUpgradeRemoved(UpgradeType upgrade);

}