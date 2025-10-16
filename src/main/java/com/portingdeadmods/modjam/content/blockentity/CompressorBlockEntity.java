package com.portingdeadmods.modjam.content.blockentity;

import com.portingdeadmods.modjam.MJConfig;
import com.portingdeadmods.modjam.capabilities.UpgradeItemHandler;
import com.portingdeadmods.modjam.content.block.UpgradeBlockEntity;
import com.portingdeadmods.modjam.content.menus.CompressorMenu;
import com.portingdeadmods.modjam.content.recipe.CompressingRecipe;
import com.portingdeadmods.modjam.data.UpgradeType;
import com.portingdeadmods.modjam.registries.MJBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.RedstoneBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.api.utils.PDLBlockStateProperties;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class CompressorBlockEntity extends ContainerBlockEntity implements MenuProvider, RedstoneBlockEntity, UpgradeBlockEntity {
    public static final Set<UpgradeType> SUPPORTED_UPGRADES = Set.of(UpgradeType.ENERGY, UpgradeType.SPEED);
    private final UpgradeItemHandler upgradeItemHandler;
    private CompressingRecipe currentRecipe;
    private int progress;
    private int maxProgress;
    private int energyUsage;
    private RedstoneSignalType redstoneSignalType;
    private long lastStampTick = -1;
    private final long animationOffset;

    public CompressorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MJBlockEntities.COMPRESSOR.get(), blockPos, blockState);
        addItemHandler(2, (slot, stack) -> slot == 0);
        addEnergyStorage(MJConfig.COMPRESSOR_CAPACITY.getAsInt());

        this.redstoneSignalType = RedstoneSignalType.IGNORED;
        this.animationOffset = (blockPos.getX() + blockPos.getY() * 7 + blockPos.getZ() * 13) & 0x7F;

        this.upgradeItemHandler = new UpgradeItemHandler(SUPPORTED_UPGRADES) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                update();
                applyUpgrades();
            }

            @Override
            public void onUpgradeAdded(UpgradeType upgrade) {
                super.onUpgradeAdded(upgrade);
                CompressorBlockEntity.this.onUpgradeAdded(upgrade);
            }

            @Override
            public void onUpgradeRemoved(UpgradeType upgrade) {
                super.onUpgradeRemoved(upgrade);
                CompressorBlockEntity.this.onUpgradeRemoved(upgrade);
            }
        };
    }
    
    public long getAnimationOffset() {
        return animationOffset;
    }

    @Override
    protected void onItemsChanged(int slot) {
        super.onItemsChanged(slot);

        if (slot == 0 || slot == 1) {
            this.checkRecipe();
        }

    }

    private void checkRecipe() {
        ItemStack stackInSlot = this.getItemHandler().getStackInSlot(0);
        CompressingRecipe recipe = level.getRecipeManager().getRecipeFor(CompressingRecipe.TYPE, new SingleRecipeInput(stackInSlot), level)
                .map(RecipeHolder::value)
                .orElse(null);
        if (recipe != null) {
            ItemStack resultStack = this.getItemHandler().getStackInSlot(1);
            if (resultStack.getCount() + recipe.result().getCount() <= this.getItemHandler().getSlotLimit(1) && (resultStack.is(recipe.result().getItem()) || resultStack.isEmpty())) {
                this.currentRecipe = recipe;
                applyUpgrades();
            } else {
                this.currentRecipe = null;
                this.progress = 0;
            }
        } else {
            this.currentRecipe = null;
            this.progress = 0;
        }
    }

    private void applyUpgrades() {
        if (currentRecipe == null) {
            this.maxProgress = 0;
            this.energyUsage = MJConfig.COMPRESSOR_USAGE.getAsInt();
            return;
        }

        float duration = currentRecipe.duration();
        float energyUsage = MJConfig.COMPRESSOR_USAGE.getAsInt();

        for (UpgradeType upgradeType : SUPPORTED_UPGRADES) {
            int count = getUpgradeAmount(upgradeType);
            if (count == 0) continue;

            for (UpgradeType.UpgradeEffect effect : upgradeType.getEffects()) {
                switch (effect.getTarget()) {
                    case DURATION -> duration = effect.apply(duration, count);
                    case ENERGY_USAGE -> energyUsage = effect.apply(energyUsage, count);
                }
            }
        }

        this.maxProgress = Math.max(1, (int) duration);
        this.energyUsage = Math.max(1, (int) energyUsage);
    }

    @Override
    public void commonTick() {
        if (this.currentRecipe != null && this.getEnergyStorage().getEnergyStored() >= energyUsage) {
            long currentTick = this.level.getGameTime();
            long tickInCycle = (currentTick + animationOffset) % 80;
            
            if (tickInCycle == 24 && lastStampTick != currentTick) {
                this.level.playSound(null, this.worldPosition, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.3f, 1.2f);
                lastStampTick = currentTick;
            }
            
            if (this.progress >= this.maxProgress) {
                this.getEnergyStorage().extractEnergy(energyUsage, false);
                ItemStack result = this.currentRecipe.result().copy();
                this.getItemHandler().extractItem(0, 1, false);
                this.forceInsertItem(1, result, false);
                this.progress = 0;
                if (this.getBlockState().getValue(PDLBlockStateProperties.ACTIVE)) {
                    this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(PDLBlockStateProperties.ACTIVE, false));
                }
            } else {
                this.progress++;
                if (!this.getBlockState().getValue(PDLBlockStateProperties.ACTIVE)) {
                    this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(PDLBlockStateProperties.ACTIVE, true));
                }
            }
        } else {
            this.progress = 0;
            if (this.getBlockState().getValue(PDLBlockStateProperties.ACTIVE)) {
                this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(PDLBlockStateProperties.ACTIVE, false));
            }
        }
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public int getProgress() {
        return this.progress;
    }

    @Override
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> blockCapability) {
        return Map.of();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.modjam.compressor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CompressorMenu(i, inventory, this);
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        this.progress = tag.getInt("progress");
        this.maxProgress = tag.getInt("maxProgress");
        this.energyUsage = tag.getInt("energyUsage");
        this.redstoneSignalType = RedstoneSignalType.values()[tag.getInt("redstone_signal_type")];
        
        if (tag.contains("upgrades")) {
            this.upgradeItemHandler.deserializeNBT(provider, tag.getCompound("upgrades"));
        }
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);
        tag.putInt("progress", this.progress);
        tag.putInt("maxProgress", this.maxProgress);
        tag.putInt("energyUsage", this.energyUsage);
        tag.putInt("redstone_signal_type", this.redstoneSignalType.ordinal());
        
        CompoundTag compoundTag = this.upgradeItemHandler.serializeNBT(provider);
        tag.put("upgrades", compoundTag);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.checkRecipe();
    }

    @Override
    public int emitRedstoneLevel() {
        return ItemHandlerHelper.calcRedstoneFromInventory(this.getItemHandler());
    }

    @Override
    public void setRedstoneSignalType(RedstoneSignalType redstoneSignalType) {
        this.redstoneSignalType = redstoneSignalType;
        this.update();
    }

    @Override
    public RedstoneSignalType getRedstoneSignalType() {
        return this.redstoneSignalType;
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
}
