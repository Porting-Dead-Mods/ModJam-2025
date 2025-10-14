package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.recipe.CompressingRecipe;
import com.portingdeadmods.modjam.content.recipe.PlanetPowerRecipe;
import com.portingdeadmods.modjam.content.recipe.PlanetSimulatorRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Modjam.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CompressingRecipe>> COMPRESSING =
            RECIPE_SERIALIZERS.register("compressing", () -> CompressingRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PlanetSimulatorRecipe>> PLANET_SIMULATOR =
            RECIPE_SERIALIZERS.register("planet_simulator", () -> PlanetSimulatorRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PlanetPowerRecipe>> PLANET_POWER =
            RECIPE_SERIALIZERS.register("planet_power", () -> PlanetPowerRecipe.Serializer.INSTANCE);
}
