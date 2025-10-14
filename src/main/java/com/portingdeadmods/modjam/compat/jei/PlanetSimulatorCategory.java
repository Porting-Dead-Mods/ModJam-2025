package com.portingdeadmods.modjam.compat.jei;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.recipe.PlanetSimulatorRecipe;
import com.portingdeadmods.modjam.registries.MJBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PlanetSimulatorCategory extends AbstractRecipeCategory<PlanetSimulatorRecipe> {
    public static final RecipeType<PlanetSimulatorRecipe> TYPE = RecipeType.create(Modjam.MODID, "planet_simulator", PlanetSimulatorRecipe.class);
    private final IPlatformFluidHelper<?> fluidHelper;

    public PlanetSimulatorCategory(IGuiHelper guiHelper, IPlatformFluidHelper<?> fluidHelper) {
        super(TYPE, Component.translatable("jei.modjam.category.planet_simulator"), guiHelper.createDrawableItemLike(MJBlocks.PLANET_SIMULATOR_CONTROLLER), 150, 80);
        this.fluidHelper = fluidHelper;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlanetSimulatorRecipe recipe, IFocusGroup focuses) {
        int inputX = 10;
        int inputY = 10;
        
        for (int i = 0; i < recipe.catalysts().size() && i < 9; i++) {
            int slotX = inputX + (i % 3) * 18;
            int slotY = inputY + (i / 3) * 18;
            builder.addInputSlot(slotX, slotY)
                    .setStandardSlotBackground()
                    .addIngredients(CompressingCategory.iWCToIngredientSaveCount(recipe.catalysts().get(i)));
        }
        
        if (recipe.fluidInput().isPresent()) {
            builder.addInputSlot(80, 10)
                    .setStandardSlotBackground()
                    .setFluidRenderer(10000, true, 16, 16)
                    .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidInput().get().getStacks()).toList());
        }
        
        int outputX = 110;
        int outputY = 10;
        
        for (int i = 0; i < recipe.outputs().size() && i < 9; i++) {
            PlanetSimulatorRecipe.WeightedOutput output = recipe.outputs().get(i);
            int slotX = outputX + (i % 3) * 18;
            int slotY = outputY + (i / 3) * 18;
            
            IRecipeSlotBuilder slot = builder.addOutputSlot(slotX, slotY)
                    .setOutputSlotBackground();
            
            if (output.itemStack().isPresent()) {
                slot.addItemStack(output.itemStack().get());
            }
            
            if (output.fluidStack().isPresent()) {
                slot.setFluidRenderer(10000, false, 16, 16)
                        .addIngredient(NeoForgeTypes.FLUID_STACK, output.fluidStack().get());
            }
            
            if (output.chance() < 1.0f) {
                slot.addTooltipCallback((view, tooltip) -> {
                    tooltip.add(Component.literal("Chance: " + (int)(output.chance() * 100) + "%"));
                });
            }
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, PlanetSimulatorRecipe recipe, IFocusGroup focuses) {
        builder.addText(Component.literal("Energy: " + recipe.energyPerTick() + " FE/t"), 10, 65);
        builder.addText(Component.literal("Duration: " + recipe.duration() + " ticks"), 10, 75);
    }
}
