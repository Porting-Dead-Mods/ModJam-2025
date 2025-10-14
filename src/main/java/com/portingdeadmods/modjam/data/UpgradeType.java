package com.portingdeadmods.modjam.data;

public enum UpgradeType {
    ENERGY("energy"),
    SPEED("speed"),
    LUCK("luck");

    private final String name;

    UpgradeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
