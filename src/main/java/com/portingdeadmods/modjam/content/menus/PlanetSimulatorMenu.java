package com.portingdeadmods.modjam.content.menus;

import com.portingdeadmods.modjam.content.blockentity.PlanetSimulatorBlockEntity;
import com.portingdeadmods.modjam.registries.MJMenus;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PlanetSimulatorMenu extends PDLAbstractContainerMenu<PlanetSimulatorBlockEntity> {
    public PlanetSimulatorMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        this(containerId, inv, (PlanetSimulatorBlockEntity) inv.player.level().getBlockEntity(byteBuf.readBlockPos()));
    }

    public PlanetSimulatorMenu(int containerId, @NotNull Inventory inv, @NotNull PlanetSimulatorBlockEntity blockEntity) {
        super(MJMenus.PLANET_SIMULATOR.get(), containerId, inv, blockEntity);
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 10, 10));
    }

    @Override
    protected int getMergeableSlotCount() {
        return 1;
    }
}
