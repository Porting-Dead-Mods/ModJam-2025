package com.portingdeadmods.modjam.content.menus;

import com.portingdeadmods.modjam.content.blockentity.CompressorBlockEntity;
import com.portingdeadmods.modjam.registries.MJMenus;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CompressorMenu extends PDLAbstractContainerMenu<CompressorBlockEntity> {
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

    }

    @Override
    protected int getMergeableSlotCount() {
        return 1;
    }
}
