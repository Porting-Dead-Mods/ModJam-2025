package com.portingdeadmods.spaceploitation.content.blockentity.bus;

import com.portingdeadmods.spaceploitation.content.menus.EnergyBusMenu;
import com.portingdeadmods.spaceploitation.data.BusType;
import com.portingdeadmods.spaceploitation.registries.MJBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class EnergyOutputBusBlockEntity extends AbstractBusBlockEntity implements MenuProvider {
    private final OutputEnergyStorage energyStorage = new OutputEnergyStorage(10000000, 10000000, 10000000);

    public EnergyOutputBusBlockEntity(BlockPos pos, BlockState blockState) {
        super(MJBlockEntities.ENERGY_OUTPUT_BUS.get(), pos, blockState, BusType.ENERGY, false);
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int addEnergy(int amount) {
        if (amount <= 0) return 0;
        return energyStorage.receiveInternal(amount, false);
    }

    @Override
    public void commonTick() {
        super.commonTick();
        
        if (level == null || level.isClientSide) return;
        if (energyStorage.getEnergyStored() <= 0) return;
        
        for (Direction direction : Direction.values()) {
            if (energyStorage.getEnergyStored() <= 0) break;
            
            BlockPos neighborPos = worldPosition.relative(direction);
            IEnergyStorage neighborEnergy = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.getOpposite());
            
            if (neighborEnergy != null && neighborEnergy.canReceive()) {
                int extracted = energyStorage.extractEnergy(Integer.MAX_VALUE, false);
                int received = neighborEnergy.receiveEnergy(extracted, false);
                int leftover = extracted - received;
                if (leftover > 0) {
                    addEnergy(leftover);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Energy", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Energy")) {
            energyStorage.receiveInternal(tag.getInt("Energy"), false);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.spaceploitation.energy_output_bus");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new EnergyBusMenu(containerId, playerInventory, this);
    }

    private class OutputEnergyStorage extends EnergyStorage {
        private boolean allowReceiveInternally = false;

        private OutputEnergyStorage(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            if (extracted > 0 && !simulate) {
                setChanged();
            }
            return extracted;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return allowReceiveInternally;
        }

        private int receiveInternal(int amount, boolean simulate) {
            allowReceiveInternally = true;
            try {
                int received = super.receiveEnergy(amount, simulate);
                if (received > 0 && !simulate) {
                    setChanged();
                }
                return received;
            } finally {
                allowReceiveInternally = false;
            }
        }
    }
}
