package com.portingdeadmods.spaceploitation.content.menus;

import com.portingdeadmods.spaceploitation.content.blockentity.bus.EnergyInputBusBlockEntity;
import com.portingdeadmods.spaceploitation.content.blockentity.bus.EnergyOutputBusBlockEntity;
import com.portingdeadmods.spaceploitation.registries.MJMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;

public class EnergyBusMenu extends AbstractContainerMenu {
    private final EnergyStorage energyStorage;
    private final ContainerData data;

    public EnergyBusMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        super(MJMenus.ENERGY_BUS.get(), containerId);
        this.energyStorage = new EnergyStorage(1000000, 10000, 10000);
        this.data = new SimpleContainerData(2);
        setupSlots(inv);
        addDataSlots(this.data);
    }

    public EnergyBusMenu(int containerId, @NotNull Inventory inv, @NotNull EnergyInputBusBlockEntity blockEntity) {
        super(MJMenus.ENERGY_BUS.get(), containerId);
        this.energyStorage = blockEntity.getEnergyStorage();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        setupSlots(inv);
        addDataSlots(this.data);
    }

    public EnergyBusMenu(int containerId, @NotNull Inventory inv, @NotNull EnergyOutputBusBlockEntity blockEntity) {
        super(MJMenus.ENERGY_BUS.get(), containerId);
        this.energyStorage = blockEntity.getEnergyStorage();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        setupSlots(inv);
        addDataSlots(this.data);
    }

    private void setupSlots(Inventory inv) {
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 83 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 141));
        }
    }

    public int getEnergyStored() {
        return data.get(0);
    }

    public int getEnergyCapacity() {
        return data.get(1);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
