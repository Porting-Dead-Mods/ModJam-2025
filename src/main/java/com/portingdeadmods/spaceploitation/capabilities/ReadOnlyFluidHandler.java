package com.portingdeadmods.spaceploitation.capabilities;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public record ReadOnlyFluidHandler(IFluidHandler inner) implements IFluidHandler {
    @Override
    public int getTanks() {
        return this.inner.getTanks();
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return this.inner.getFluidInTank(i);
    }

    @Override
    public int getTankCapacity(int i) {
        return this.inner.getTankCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return this.inner.isFluidValid(i, fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return FluidStack.EMPTY;
    }

}
