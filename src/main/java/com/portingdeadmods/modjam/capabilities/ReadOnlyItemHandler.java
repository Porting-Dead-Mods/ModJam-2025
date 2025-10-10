package com.portingdeadmods.modjam.capabilities;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public record ReadOnlyItemHandler(IItemHandler inner) implements IItemHandler {
    @Override
    public int getSlots() {
        return this.inner.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.inner.getStackInSlot(i);
    }

    @Override
    public ItemStack insertItem(int i, ItemStack itemStack, boolean b) {
        return itemStack;
    }

    @Override
    public ItemStack extractItem(int i, int i1, boolean b) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int i) {
        return this.inner.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int i, ItemStack itemStack) {
        return this.inner.isItemValid(i, itemStack);
    }
}
