package com.portingdeadmods.modjam.capabilities;

import com.portingdeadmods.modjam.content.items.UpgradeItem;
import com.portingdeadmods.modjam.data.UpgradeType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Set;
import java.util.function.Supplier;

public class UpgradeItemHandler extends ItemStackHandler {
    private final Set<UpgradeType> supportedUpgrades;

    public UpgradeItemHandler(int slots, Set<UpgradeType> supportedUpgrades) {
        super(slots);
        this.supportedUpgrades = supportedUpgrades;
    }

    public UpgradeItemHandler(Set<UpgradeType> supportedUpgrades) {
        this(4, supportedUpgrades);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof UpgradeItem upgradeItem && this.supportedUpgrades.contains(upgradeItem.getUpgradeType());
    }

    @Override
    public int getSlotLimit(int slot) {
        return 4;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack remainder = super.insertItem(slot, stack, simulate);
        if (stack.getItem() instanceof UpgradeItem upgradeItem && !simulate && remainder.getCount() < stack.getCount()) {
            UpgradeType upgrade = upgradeItem.getUpgradeType();
            this.onUpgradeAdded(upgrade);
        }
        return remainder;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack extracted = super.extractItem(slot, amount, simulate);
        if (extracted.getItem() instanceof UpgradeItem upgradeItem && !simulate) {
            UpgradeType upgrade = upgradeItem.getUpgradeType();
            this.onUpgradeRemoved(upgrade);
        }
        return extracted;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        ItemStack previous = this.getStackInSlot(slot);

        boolean wasUpgrade = previous.getItem() instanceof UpgradeItem;
        boolean isUpgrade = stack.getItem() instanceof UpgradeItem;

        // Set the new stack
        super.setStackInSlot(slot, stack);

        // If the old item was an upgrade but the new one is not (or empty), we removed an upgrade
        if (wasUpgrade && (!isUpgrade || stack.isEmpty())) {
            UpgradeType upgrade = ((UpgradeItem) previous.getItem()).getUpgradeType();
            this.onUpgradeRemoved(upgrade);
        }

        // If the new item is an upgrade and the old one was not, we added an upgrade
        if (isUpgrade && (!wasUpgrade || previous.isEmpty())) {
            UpgradeType upgrade = ((UpgradeItem) stack.getItem()).getUpgradeType();
            this.onUpgradeAdded(upgrade);
        }
    }

    public void onUpgradeAdded(UpgradeType upgrade) {
    }

    public void onUpgradeRemoved(UpgradeType upgrade) {
    }
}