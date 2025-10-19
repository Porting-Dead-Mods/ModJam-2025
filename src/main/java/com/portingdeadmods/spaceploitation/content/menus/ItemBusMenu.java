package com.portingdeadmods.spaceploitation.content.menus;

import com.portingdeadmods.spaceploitation.content.blockentity.bus.ItemInputBusBlockEntity;
import com.portingdeadmods.spaceploitation.content.blockentity.bus.ItemOutputBusBlockEntity;
import com.portingdeadmods.spaceploitation.registries.MJMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ItemBusMenu extends AbstractContainerMenu {
    private final ItemStackHandler itemHandler;
    private final boolean isOutput;

    public ItemBusMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        super(MJMenus.ITEM_BUS.get(), containerId);
        var be = inv.player.level().getBlockEntity(byteBuf.readBlockPos());
        if (be instanceof ItemInputBusBlockEntity inputBus) {
            this.itemHandler = inputBus.getItemHandler();
            this.isOutput = false;
        } else if (be instanceof ItemOutputBusBlockEntity outputBus) {
            this.itemHandler = outputBus.getItemHandler();
            this.isOutput = true;
        } else {
            throw new IllegalStateException("Invalid block entity type for ItemBusMenu");
        }
        setupSlots(inv);
    }

    public ItemBusMenu(int containerId, @NotNull Inventory inv, @NotNull ItemInputBusBlockEntity blockEntity) {
        super(MJMenus.ITEM_BUS.get(), containerId);
        this.itemHandler = blockEntity.getItemHandler();
        this.isOutput = false;
        setupSlots(inv);
    }

    public ItemBusMenu(int containerId, @NotNull Inventory inv, @NotNull ItemOutputBusBlockEntity blockEntity) {
        super(MJMenus.ITEM_BUS.get(), containerId);
        this.itemHandler = blockEntity.getItemHandler();
        this.isOutput = true;
        setupSlots(inv);
    }

    private void setupSlots(Inventory inv) {
        int startX = 62;
        int startY = 17;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                addSlot(new SlotItemHandler(itemHandler, index, startX + col * 18, startY + row * 18));
            }
        }

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

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            stack = stackInSlot.copy();

            int invBase = 9;
            int invFull = slots.size();
            int invHotbar = invFull - 9;
            int invPlayer = invHotbar - 27;

            if (index < invPlayer) {
                if (!moveItemStackTo(stackInSlot, invPlayer, invFull, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!isOutput) {
                    if (!moveItemStackTo(stackInSlot, 0, invBase, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }
        return stack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
