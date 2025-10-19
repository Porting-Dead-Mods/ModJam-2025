package com.portingdeadmods.spaceploitation.compat.guideme;

import com.portingdeadmods.spaceploitation.content.recipe.CompressingRecipe;
import com.portingdeadmods.spaceploitation.content.recipe.PlanetPowerRecipe;
import com.portingdeadmods.spaceploitation.content.recipe.PlanetSimulatorRecipe;
import com.portingdeadmods.spaceploitation.registries.MJBlocks;
import guideme.compiler.tags.RecipeTypeMappingSupplier;
import guideme.document.block.LytSlotGrid;
import guideme.document.block.recipes.LytStandardRecipeBox;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class RecipeTypeContributions implements RecipeTypeMappingSupplier {
    @Override
    public void collect(RecipeTypeMappings mappings) {
        mappings.add(CompressingRecipe.TYPE, RecipeTypeContributions::compressing);
        mappings.add(PlanetPowerRecipe.TYPE, RecipeTypeContributions::planetPower);
        mappings.add(PlanetSimulatorRecipe.TYPE, RecipeTypeContributions::planetSimulator);
    }

    private static LytStandardRecipeBox<CompressingRecipe> compressing(RecipeHolder<CompressingRecipe> holder) {
        return LytStandardRecipeBox.builder()
                .icon(MJBlocks.COMPRESSOR.get())
                .title(MJBlocks.COMPRESSOR.get().asItem().getDescription().getString())
                .input(Ingredient.of(holder.value().ingredient().ingredient().getItems()))
                .outputFromResultOf(holder)
                .build(holder);
    }

    private static LytStandardRecipeBox<PlanetPowerRecipe> planetPower(RecipeHolder<PlanetPowerRecipe> holder) {
        List<Ingredient> inputs = new ArrayList<>();
        for (var input : holder.value().inputs()) {
            inputs.add(Ingredient.of(input.ingredient().getItems()));
        }
        
        return LytStandardRecipeBox.builder()
                .icon(MJBlocks.PLANET_SIMULATOR_CONTROLLER.get())
                .title("Planet Power Generation")
                .input(LytSlotGrid.row(inputs, false))
                .build(holder);
    }

    private static LytStandardRecipeBox<PlanetSimulatorRecipe> planetSimulator(RecipeHolder<PlanetSimulatorRecipe> holder) {
        List<Ingredient> inputs = new ArrayList<>();
        for (var input : holder.value().inputs()) {
            inputs.add(Ingredient.of(input.ingredient().getItems()));
        }

        List<Ingredient> outputs = new ArrayList<>();
        for (var output : holder.value().outputs()) {
            if (output.itemStack().isPresent()) {
                outputs.add(Ingredient.of(output.itemStack().get()));
            }
        }

        return LytStandardRecipeBox.builder()
                .icon(MJBlocks.PLANET_SIMULATOR_CONTROLLER.get())
                .title("Planet Simulator")
                .input(LytSlotGrid.row(inputs, false))
                .output(LytSlotGrid.row(outputs, false))
                .build(holder);
    }
}
