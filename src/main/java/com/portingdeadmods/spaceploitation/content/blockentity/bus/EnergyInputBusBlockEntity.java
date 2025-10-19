package com.portingdeadmods.spaceploitation.content.blockentity.bus;

import com.portingdeadmods.spaceploitation.content.menus.EnergyBusMenu;
import com.portingdeadmods.spaceploitation.data.BusType;
import com.portingdeadmods.spaceploitation.registries.MJBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;

public class EnergyInputBusBlockEntity extends AbstractBusBlockEntity implements MenuProvider {
    private final EnergyStorage energyStorage = new EnergyStorage(1000000, 10000000, 100000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) {
                setChanged();
            }
            return received;
        }
    };

    public EnergyInputBusBlockEntity(BlockPos pos, BlockState blockState) {
        super(MJBlockEntities.ENERGY_INPUT_BUS.get(), pos, blockState, BusType.ENERGY, true);
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
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
            energyStorage.receiveEnergy(tag.getInt("Energy"), false);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.spaceploitation.energy_input_bus");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new EnergyBusMenu(containerId, playerInventory, this);
    }
}
