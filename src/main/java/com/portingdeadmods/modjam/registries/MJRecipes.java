package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.recipe.GrindingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Modjam.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrindingRecipe>> GRINDING =
            RECIPE_SERIALIZERS.register("grinding", () -> new SimpleCraftingRecipeSerializer<>(GrindingRecipe::new));
}
