package com.portingdeadmods.modjam.datagen.recipe.builder;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.recipe.PlanetSimulatorRecipe;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlanetSimulatorRecipeBuilder implements RecipeBuilder {
    private final ResourceLocation id;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @javax.annotation.Nullable
    private String group;

    private ResourceKey<PlanetType> planetType;
    private List<IngredientWithCount> catalysts, inputs;
    private Optional<FluidIngredient> fluidInput;
    private int energyPerTick, duration;
    private List<PlanetSimulatorRecipe.WeightedOutput> outputs;

    public PlanetSimulatorRecipeBuilder(ResourceLocation id, ResourceKey<PlanetType> planetType, List<IngredientWithCount> catalysts, List<IngredientWithCount> inputs, Optional<FluidIngredient> fluidInput, int energyPerTick, int duration, List<PlanetSimulatorRecipe.WeightedOutput> outputs) {
        this.id = id;
        this.planetType = planetType;
        this.catalysts = catalysts;
        this.inputs = inputs;
        this.fluidInput = fluidInput;
        this.energyPerTick = energyPerTick;
        this.duration = duration;
        this.outputs = outputs;
    }

    public PlanetSimulatorRecipeBuilder(ResourceLocation id, ResourceKey<PlanetType> planetType) {
        this(id, planetType, new ArrayList<>(), new ArrayList<>(), Optional.empty(), 0, 0, new ArrayList<>());
    }

    public static PlanetSimulatorRecipeBuilder recipe(String path, ResourceKey<PlanetType> planetType) {
        return new PlanetSimulatorRecipeBuilder(Modjam.rl(path), planetType);
    }

    public PlanetSimulatorRecipeBuilder catalysts(IngredientWithCount... catalysts) {
        this.catalysts.addAll(List.of(catalysts));
        return this;
    }

    public PlanetSimulatorRecipeBuilder catalyst(Item item, int count) {
        IngredientWithCount ingredient = new IngredientWithCount(Ingredient.of(item), count);
        this.catalysts.add(ingredient);
        return this;
    }

    public PlanetSimulatorRecipeBuilder inputs(IngredientWithCount... inputs) {
        this.inputs.addAll(List.of(inputs));
        return this;
    }

    public PlanetSimulatorRecipeBuilder input(ItemLike item, int count) {
        IngredientWithCount ingredient = new IngredientWithCount(Ingredient.of(item), count);
        this.inputs.add(ingredient);
        return this;
    }

    public PlanetSimulatorRecipeBuilder fluid(FluidIngredient fluidInput) {
        this.fluidInput = Optional.of(fluidInput);
        return this;
    }

    public PlanetSimulatorRecipeBuilder results(PlanetSimulatorRecipe.WeightedOutput... outputs) {
        this.outputs.addAll(List.of(outputs));
        return this;
    }

    public PlanetSimulatorRecipeBuilder result(ItemStack itemStack, float chance) {
        this.outputs.add(new PlanetSimulatorRecipe.WeightedOutput(Optional.of(itemStack), Optional.empty(), chance));
        return this;
    }

    public PlanetSimulatorRecipeBuilder result(ItemLike item, int count, float chance) {
        ItemStack itemStack = new ItemStack(item, count);
        this.outputs.add(new PlanetSimulatorRecipe.WeightedOutput(Optional.of(itemStack), Optional.empty(), chance));
        return this;
    }

    public PlanetSimulatorRecipeBuilder result(ItemLike item, float chance) {
        return result(item, 1, chance);
    }

    public PlanetSimulatorRecipeBuilder result(Fluid fluid, int amount, float chance) {
        FluidStack fluidStack = new FluidStack(fluid, amount);
        this.outputs.add(new PlanetSimulatorRecipe.WeightedOutput(Optional.empty(), Optional.of(fluidStack), chance));
        return this;
    }

    public PlanetSimulatorRecipeBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public PlanetSimulatorRecipeBuilder setEnergyConsumption(int energyPerTick) {
        this.energyPerTick = energyPerTick;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, Criterion<?> pCriterionTrigger) {
        this.criteria.put(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    public void save(RecipeOutput recipeOutput) {
        this.save(recipeOutput, this.id);
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        Advancement.Builder advancement$builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pId))
                .rewards(AdvancementRewards.Builder.recipe(pId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        PlanetSimulatorRecipe recipe = new PlanetSimulatorRecipe(planetType,catalysts,inputs,fluidInput,energyPerTick,duration,outputs);
        ResourceLocation pathedLoc = pId.withPrefix("planet_simulator/%s/".formatted(planetType.location().getPath()));
        pRecipeOutput.accept(pathedLoc, recipe,advancement$builder.build(pathedLoc.withPrefix("recipes/")));
    }
}
