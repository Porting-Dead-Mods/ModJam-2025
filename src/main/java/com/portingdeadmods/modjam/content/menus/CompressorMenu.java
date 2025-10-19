package com.portingdeadmods.modjam.content.menus;

import com.portingdeadmods.modjam.content.blockentity.CompressorBlockEntity;
import com.portingdeadmods.modjam.content.items.UpgradeItem;
import com.portingdeadmods.modjam.registries.MJMenus;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CompressorMenu extends PDLAbstractContainerMenu<CompressorBlockEntity> {
    private final List<UpgradeSlot> upgradeSlots;

    public CompressorMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (CompressorBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    public CompressorMenu(int containerId, @NotNull Inventory inv, @NotNull CompressorBlockEntity blockEntity) {
        super(MJMenus.COMPRESSOR.get(), containerId, inv, blockEntity);

        int startX = 56;
        int startY = 35;
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, startX, startY));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, startX + 60, startY));

        addPlayerInventory(inv, 83 + 1);
        addPlayerHotbar(inv, 141 + 1);

        this.upgradeSlots = new ArrayList<>();
        for (int i = 0; i < blockEntity.getUpgradeItemHandler().getSlots(); i++) {
            UpgradeSlot slot = new UpgradeSlot(blockEntity.getUpgradeItemHandler(), i, 179, 27 + 22 + i * 20);
            slot.setActive(false);
            this.addSlot(slot);
            this.upgradeSlots.add(slot);
        }
    }

    public List<UpgradeSlot> getUpgradeSlots() {
        return upgradeSlots;
    }

    @Override
    protected int getMergeableSlotCount() {
        return 2 + upgradeSlots.size();
    }

    @Override
    protected boolean performMerge(int index, ItemStack stack) {
        int mainSlot = 2;
        int invFull = slots.size();
        int upgradeStart = invFull - upgradeSlots.size();
        int invHotbar = upgradeStart - 9;
        int invPlayer = invHotbar - 27;

        if (index >= upgradeStart) {
            return moveItemStackTo(stack, invPlayer, upgradeStart, true);
        } else if (index >= invPlayer && index < upgradeStart) {
            if (stack.getItem() instanceof UpgradeItem) {
                if (moveItemStackTo(stack, upgradeStart, invFull, false)) {
                    return true;
                }
            }
            return moveItemStackTo(stack, 0, mainSlot, false);
        } else {
            return moveItemStackTo(stack, invPlayer, invHotbar, false);
        }
    }

    public void setUpgradeSlotPositions(int startY) {
        List<UpgradeSlot> upgradeSlots = this.getUpgradeSlots();
        for (int i = 0; i < upgradeSlots.size(); i++) {
            UpgradeSlot upgradeSlot = upgradeSlots.get(i);
            ((SlotAccessor) upgradeSlot).setY(startY + i * 20);
        }
    }
}
