package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.modjam.registries.MJItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MJRecipeProvider extends RecipeProvider {

    public MJRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> TANTALUM_SMELTABLES = List.of(
                MJItems.RAW_TANTALUM.get(),
                MJItems.TANTALUM_ORE.get(),
                MJItems.DEEPSLATE_TANTALUM_ORE.get()
        );
        
        oreSmelting(recipeOutput, TANTALUM_SMELTABLES, RecipeCategory.MISC, MJItems.TANTALUM_INGOT.get(), 0.7f, 200, "tantalum_ingot");
        oreBlasting(recipeOutput, TANTALUM_SMELTABLES, RecipeCategory.MISC, MJItems.TANTALUM_INGOT.get(), 0.7f, 100, "tantalum_ingot");
        
        nineBlockStorageRecipes(recipeOutput, RecipeCategory.MISC, MJItems.TANTALUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, MJBlocks.TANTALUM_STORAGE_BLOCK.get());
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MJItems.TANTALUM_INGOT.get())
                .requires(MJItems.TANTALUM_NUGGET.get(), 9)
                .unlockedBy("has_tantalum_nugget", has(MJItems.TANTALUM_NUGGET.get()))
                .save(recipeOutput, Modjam.MODID + ":tantalum_ingot_from_nuggets");
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MJItems.TANTALUM_NUGGET.get(), 9)
                .requires(MJItems.TANTALUM_INGOT.get())
                .unlockedBy("has_tantalum_ingot", has(MJItems.TANTALUM_INGOT.get()))
                .save(recipeOutput);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.HAMMER.get())
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .define('I', MJItems.TANTALUM_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_tantalum_ingot", has(MJItems.TANTALUM_INGOT.get()))
                .save(recipeOutput);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.PESTLE.get())
                .pattern(" S ")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))
                .save(recipeOutput);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MJItems.MORTAR.get())
                .pattern("S S")
                .pattern("S S")
                .pattern(" S ")
                .define('S', Items.STONE)
                .unlockedBy("has_stone", has(Items.STONE))
                .save(recipeOutput);
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MJItems.TANTALUM_SHEET.get())
                .pattern("H")
                .pattern("I")
                .pattern("I")
                .define('H', MJItems.HAMMER.get())
                .define('I', MJItems.TANTALUM_INGOT.get())
                .unlockedBy("has_hammer", has(MJItems.HAMMER.get()))
                .save(recipeOutput);
    }
    
    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(itemlike), category, result, experience, cookingTime)
                    .group(group)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, Modjam.MODID + ":" + getItemName(result) + "_from_smelting_" + getItemName(itemlike));
        }
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(itemlike), category, result, experience, cookingTime)
                    .group(group)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, Modjam.MODID + ":" + getItemName(result) + "_from_blasting_" + getItemName(itemlike));
        }
    }
}
