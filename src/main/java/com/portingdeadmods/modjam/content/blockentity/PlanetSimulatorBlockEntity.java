package com.portingdeadmods.modjam.content.blockentity;

import com.portingdeadmods.modjam.capabilities.ReadOnlyEnergyStorage;
import com.portingdeadmods.modjam.capabilities.ReadOnlyFluidHandler;
import com.portingdeadmods.modjam.capabilities.ReadOnlyItemHandler;
import com.portingdeadmods.modjam.capabilities.UpgradeItemHandler;
import com.portingdeadmods.modjam.content.block.UpgradeBlockEntity;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import com.portingdeadmods.modjam.data.UpgradeType;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.modjam.registries.MJDataComponents;
import com.portingdeadmods.modjam.registries.MJMenus;
import com.portingdeadmods.modjam.registries.MJMultiblocks;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class PlanetSimulatorBlockEntity extends ContainerBlockEntity implements MultiblockEntity, MenuProvider, UpgradeBlockEntity {
    public static final Set<UpgradeType> SUPPORTED_UPGRADES = Set.of(UpgradeType.values());
    private MultiblockData multiblockData;
    private final UpgradeItemHandler upgradeItemHandler;

    public PlanetSimulatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MJBlockEntities.PLANET_SIMULATOR.get(), blockPos, blockState);
        this.addEnergyStorage(10_000_000);
        this.addItemHandler(1, (slot, item) -> item.has(MJDataComponents.PLANET));
        this.addFluidTank(16_000);

        this.upgradeItemHandler = new UpgradeItemHandler(SUPPORTED_UPGRADES) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);

                update();
            }

            @Override
            public void onUpgradeAdded(UpgradeType upgrade) {
                super.onUpgradeAdded(upgrade);

                PlanetSimulatorBlockEntity.this.onUpgradeAdded(upgrade);
            }

            @Override
            public void onUpgradeRemoved(UpgradeType upgrade) {
                super.onUpgradeRemoved(upgrade);

                PlanetSimulatorBlockEntity.this.onUpgradeRemoved(upgrade);
            }
        };

    }

    @Override
    public void commonTick() {
        super.commonTick();

        if (this.level.getGameTime() % 20 == 0 && !this.getBlockState().getValue(Multiblock.FORMED)) {
            MJMultiblocks.PLANET_SIMULATOR.get().form(this.level, this.worldPosition);
        }

    }

    public ReadOnlyEnergyStorage getEnergyStorageReadOnly(Direction direction) {
        return new ReadOnlyEnergyStorage(this.getEnergyStorage());
    }

    public ReadOnlyItemHandler getItemHandlerReadOnly(Direction direction) {
        return new ReadOnlyItemHandler(this.getItemHandler());
    }

    public ReadOnlyFluidHandler getFluidHandlerReadOnly(Direction direction) {
        return new ReadOnlyFluidHandler(this.getFluidHandler());
    }

    @Override
    public UpgradeItemHandler getUpgradeItemHandler() {
        return upgradeItemHandler;
    }

    @Override
    public Set<UpgradeType> getSupportedUpgrades() {
        return SUPPORTED_UPGRADES;
    }

    @Override
    public boolean hasUpgrade(UpgradeType upgrade) {
        for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
            if (this.getUpgradeItemHandler().getStackInSlot(i).is(upgrade.getItem())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getUpgradeAmount(UpgradeType upgrade) {
        int amount = 0;
        Item upgradeItem = upgrade.getItem();

        for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
            ItemStack stackInSlot = this.getUpgradeItemHandler().getStackInSlot(i);
            if (stackInSlot.is(upgradeItem)) {
                amount += stackInSlot.getCount();
            }
        }

        return amount;
    }

    @Override
    public void onUpgradeAdded(UpgradeType upgrade) {

    }

    @Override
    public void onUpgradeRemoved(UpgradeType upgrade) {

    }

    @Override
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> blockCapability) {
        return Map.of();
    }

    @Override
    public MultiblockData getMultiblockData() {
        return this.multiblockData;
    }

    @Override
    public void setMultiblockData(MultiblockData multiblockData) {
        this.multiblockData = multiblockData;
        this.setChanged();
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider registries) {
        if (this.getMultiblockData() != null) {
            tag.put("multiblock_data", this.saveMBData());
        }

        CompoundTag compoundTag = this.upgradeItemHandler.serializeNBT(registries);
        tag.put("upgrades", compoundTag);

    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("multiblock_data")) {
            this.multiblockData = this.loadMBData(tag.getCompound("multiblock_data"));
        }

        if (tag.contains("upgrades")) {
            this.upgradeItemHandler.deserializeNBT(registries, tag.getCompound("upgrades"));
        }

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Planet Simulator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PlanetSimulatorMenu(i, inventory, this);
    }
}
