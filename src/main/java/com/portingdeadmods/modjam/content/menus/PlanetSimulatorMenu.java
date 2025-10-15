package com.portingdeadmods.modjam.content.menus;

import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBlockEntity;
import com.portingdeadmods.modjam.registries.MJMenus;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlanetSimulatorMenu extends PDLAbstractContainerMenu<PlanetSimulatorBlockEntity> {
    private final List<UpgradeSlot> upgradeSlots;
    private SlotItemHandler planetCardSlot;

    public PlanetSimulatorMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (PlanetSimulatorBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    public PlanetSimulatorMenu(int containerId, @NotNull Inventory inv, @NotNull PlanetSimulatorBlockEntity blockEntity) {
        super(MJMenus.PLANET_SIMULATOR.get(), containerId, inv, blockEntity);
        SlotItemHandler planetCardSlot = new SlotItemHandler(blockEntity.getItemHandler(), 0, 179, 32);
        this.planetCardSlot = planetCardSlot;
        this.addSlot(planetCardSlot);

        this.addPlayerInventory(inv, 83 + 33);
        this.addPlayerHotbar(inv, 141 + 33);

        this.upgradeSlots = new ArrayList<>();
        for (int i = 0; i < blockEntity.getUpgradeItemHandler().getSlots(); i++) {
            UpgradeSlot slot = new UpgradeSlot(blockEntity.getUpgradeItemHandler(), i, 179, 27 + i * 20);
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
        return 1;
    }

    public void setUpgradeSlotPositions(int startY) {
        List<UpgradeSlot> upgradeSlots = this.getUpgradeSlots();
        for (int i = 0; i < upgradeSlots.size(); i++) {
            UpgradeSlot upgradeSlot = upgradeSlots.get(i);
            ((SlotAccessor) upgradeSlot).setY(startY + i * 20);
        }
    }

    public void setPlanetCardSlotPosition(int y) {
        ((SlotAccessor) this.planetCardSlot).setY(y);
    }

}
