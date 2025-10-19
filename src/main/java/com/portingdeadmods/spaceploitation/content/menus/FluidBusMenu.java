package com.portingdeadmods.spaceploitation.content.menus;

import com.portingdeadmods.spaceploitation.content.blockentity.bus.FluidInputBusBlockEntity;
import com.portingdeadmods.spaceploitation.content.blockentity.bus.FluidOutputBusBlockEntity;
import com.portingdeadmods.spaceploitation.registries.MJMenus;
import com.portingdeadmods.portingdeadlibs.api.capabilities.DynamicFluidTank;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class FluidBusMenu extends AbstractContainerMenu {
    private final DynamicFluidTank fluidTank;
    private final ContainerData data;
    private FluidStack clientFluidStack = FluidStack.EMPTY;

    public FluidBusMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf byteBuf) {
        super(MJMenus.FLUID_BUS.get(), containerId);
        this.fluidTank = new DynamicFluidTank(16000);
        this.data = new SimpleContainerData(3);
        setupSlots(inv);
        addDataSlots(this.data);
    }

    public FluidBusMenu(int containerId, @NotNull Inventory inv, @NotNull FluidInputBusBlockEntity blockEntity) {
        super(MJMenus.FLUID_BUS.get(), containerId);
        this.fluidTank = blockEntity.getFluidTank();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                FluidStack fluid = fluidTank.getFluid();
                return switch (index) {
                    case 0 -> fluid.getAmount() & 0xFFFF;
                    case 1 -> (fluid.getAmount() >> 16) & 0xFFFF;
                    case 2 -> BuiltInRegistries.FLUID.getId(fluid.getFluid());
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        setupSlots(inv);
        addDataSlots(this.data);
    }

    public FluidBusMenu(int containerId, @NotNull Inventory inv, @NotNull FluidOutputBusBlockEntity blockEntity) {
        super(MJMenus.FLUID_BUS.get(), containerId);
        this.fluidTank = blockEntity.getFluidTank();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                FluidStack fluid = fluidTank.getFluid();
                return switch (index) {
                    case 0 -> fluid.getAmount() & 0xFFFF;
                    case 1 -> (fluid.getAmount() >> 16) & 0xFFFF;
                    case 2 -> BuiltInRegistries.FLUID.getId(fluid.getFluid());
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        setupSlots(inv);
        addDataSlots(this.data);
    }

    private void setupSlots(Inventory inv) {
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

    public FluidStack getFluidStack() {
        int amount = (data.get(1) << 16) | (data.get(0) & 0xFFFF);
        int fluidId = data.get(2);
        
        if (fluidId == 0 || amount == 0) {
            clientFluidStack = FluidStack.EMPTY;
        } else {
            var fluid = BuiltInRegistries.FLUID.byId(fluidId);
            if (clientFluidStack.isEmpty() || !clientFluidStack.is(fluid)) {
                clientFluidStack = new FluidStack(fluid, amount);
            } else {
                clientFluidStack.setAmount(amount);
            }
        }
        
        return clientFluidStack;
    }

    public int getFluidCapacity() {
        return 16000;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
