package com.portingdeadmods.modjam.capabilities;

import net.neoforged.neoforge.energy.IEnergyStorage;

public record ReadOnlyEnergyStorage(IEnergyStorage inner) implements IEnergyStorage {
    @Override
    public int receiveEnergy(int i, boolean b) {
        return 0;
    }

    @Override
    public int extractEnergy(int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return this.inner.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.inner.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return this.inner.canExtract();
    }

    @Override
    public boolean canReceive() {
        return this.inner.canReceive();
    }
}
