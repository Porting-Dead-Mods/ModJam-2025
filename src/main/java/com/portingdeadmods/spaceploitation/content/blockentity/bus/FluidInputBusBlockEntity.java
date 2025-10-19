package com.portingdeadmods.spaceploitation.content.blockentity.bus;

import com.portingdeadmods.spaceploitation.content.menus.FluidBusMenu;
import com.portingdeadmods.spaceploitation.data.BusType;
import com.portingdeadmods.spaceploitation.registries.MJBlockEntities;
import com.portingdeadmods.portingdeadlibs.api.capabilities.DynamicFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FluidInputBusBlockEntity extends AbstractBusBlockEntity implements MenuProvider {
    private final DynamicFluidTank fluidTank = new DynamicFluidTank(16000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    public FluidInputBusBlockEntity(BlockPos pos, BlockState blockState) {
        super(MJBlockEntities.FLUID_INPUT_BUS.get(), pos, blockState, BusType.FLUID, true);
    }

    public DynamicFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("FluidTank", fluidTank.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("FluidTank")) {
            fluidTank.deserializeNBT(registries, tag.getCompound("FluidTank"));
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.spaceploitation.fluid_input_bus");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FluidBusMenu(containerId, playerInventory, this);
    }
}
