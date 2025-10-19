package com.portingdeadmods.spaceploitation.data;

public enum BusType {
    ENERGY,
    ITEM,
    FLUID;

    public String getName() {
        return name().toLowerCase();
    }
}
