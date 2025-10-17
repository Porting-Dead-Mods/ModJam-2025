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
import net.neoforged.neoforge.common.Tags;

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
                MJBlocks.TANTALUM_ORE.get(),
                MJBlocks.DEEPSLATE_TANTALUM_ORE.get()
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
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MJItems.PLANET_CARD.get())
                .requires(MJItems.PLANET_CARD.get())
                .unlockedBy("has_planet_card", has(MJItems.PLANET_CARD.get()))
                .save(recipeOutput, Modjam.MODID + ":unlink_planet_card");
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MJItems.PLANET_CARD.get())
                .requires(MJItems.TINTED_PLANET_CARD.get())
                .unlockedBy("has_tinted_planet_card", has(MJItems.TINTED_PLANET_CARD.get()))
                .save(recipeOutput, Modjam.MODID + ":unlink_tinted_planet_card");

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MJBlocks.COMPRESSOR.get())
                .pattern("IPI")
                .pattern("PBP")
                .pattern("IRI")
                .define('I', MJItems.TANTALUM_INGOT)
                .define('P', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('B', MJBlocks.PLANET_SIMULATOR_FRAME.get().asItem())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_tantalum_ingot", has(MJItems.TANTALUM_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MJBlocks.PLANET_SIMULATOR_CASING.get(), 4)
                .pattern("IPI")
                .pattern("P P")
                .pattern("IPI")
                .define('I', MJItems.TANTALUM_INGOT)
                .define('P', MJItems.TANTALUM_SHEET)
                .unlockedBy("has_tantalum_sheet", has(MJItems.TANTALUM_SHEET))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MJBlocks.PLANET_SIMULATOR_FRAME.get(), 8)
                .pattern("III")
                .pattern("I I")
                .pattern("III")
                .define('I', MJItems.TANTALUM_INGOT)
                .unlockedBy("has_tantalum_ingot", has(MJItems.TANTALUM_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MJBlocks.PLANET_SIMULATOR_CONTROLLER.get())
                .pattern(" C ")
                .pattern("CDC")
                .pattern(" C ")
                .define('C', MJBlocks.PLANET_SIMULATOR_CASING)
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .unlockedBy("has_casing", has(MJBlocks.PLANET_SIMULATOR_CASING))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MJItems.PLANET_CARD.get())
                .pattern(" P ")
                .pattern("PIP")
                .pattern(" G ")
                .define('I', MJItems.TANTALUM_INGOT)
                .define('P', MJItems.TANTALUM_SHEET)
                .define('G', Tags.Items.NUGGETS_GOLD)
                .unlockedBy("has_tantalum_sheet", has(MJItems.TANTALUM_SHEET))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MJBlocks.ENERGY_INPUT_BUS.get())
                .pattern("IRI")
                .pattern("RCR")
                .pattern("IRI")
                .define('I', MJItems.TANTALUM_INGOT)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', MJBlocks.PLANET_SIMULATOR_CASING)
                .unlockedBy("has_casing", has(MJBlocks.PLANET_SIMULATOR_CASING))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MJBlocks.ENERGY_OUTPUT_BUS.get())
                .requires(MJBlocks.ENERGY_INPUT_BUS.get())
                .unlockedBy("has_energy_input_bus", has(MJBlocks.ENERGY_INPUT_BUS.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MJBlocks.ENERGY_INPUT_BUS.get())
                .requires(MJBlocks.ENERGY_OUTPUT_BUS.get())
                .unlockedBy("has_energy_output_bus", has(MJBlocks.ENERGY_OUTPUT_BUS.get()))
                .save(recipeOutput, Modjam.MODID + ":energy_input_bus_from_output");

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MJBlocks.ITEM_INPUT_BUS.get())
                .pattern("IPI")
                .pattern("PCP")
                .pattern("IPI")
                .define('I', MJItems.TANTALUM_INGOT)
                .define('P', Tags.Items.CHESTS_WOODEN)
                .define('C', MJBlocks.PLANET_SIMULATOR_CASING)
                .unlockedBy("has_casing", has(MJBlocks.PLANET_SIMULATOR_CASING))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MJBlocks.ITEM_OUTPUT_BUS.get())
                .requires(MJBlocks.ITEM_INPUT_BUS.get())
                .unlockedBy("has_item_input_bus", has(MJBlocks.ITEM_INPUT_BUS.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MJBlocks.ITEM_INPUT_BUS.get())
                .requires(MJBlocks.ITEM_OUTPUT_BUS.get())
                .unlockedBy("has_item_output_bus", has(MJBlocks.ITEM_OUTPUT_BUS.get()))
                .save(recipeOutput, Modjam.MODID + ":item_input_bus_from_output");

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, MJBlocks.FLUID_INPUT_BUS.get())
                .pattern("IBI")
                .pattern("BCB")
                .pattern("IBI")
                .define('I', MJItems.TANTALUM_INGOT)
                .define('B', Items.BUCKET)
                .define('C', MJBlocks.PLANET_SIMULATOR_CASING)
                .unlockedBy("has_casing", has(MJBlocks.PLANET_SIMULATOR_CASING))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MJBlocks.FLUID_OUTPUT_BUS.get())
                .requires(MJBlocks.FLUID_INPUT_BUS.get())
                .unlockedBy("has_fluid_input_bus", has(MJBlocks.FLUID_INPUT_BUS.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, MJBlocks.FLUID_INPUT_BUS.get())
                .requires(MJBlocks.FLUID_OUTPUT_BUS.get())
                .unlockedBy("has_fluid_output_bus", has(MJBlocks.FLUID_OUTPUT_BUS.get()))
                .save(recipeOutput, Modjam.MODID + ":fluid_input_bus_from_output");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MJItems.ENERGY_UPGRADE.get())
                .pattern("RGR")
                .pattern("GPG")
                .pattern("RGR")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('P', MJItems.TANTALUM_SHEET)
                .unlockedBy("has_tantalum_sheet", has(MJItems.TANTALUM_SHEET))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MJItems.SPEED_UPGRADE.get())
                .pattern("RSR")
                .pattern("SPS")
                .pattern("RSR")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('S', Items.SUGAR)
                .define('P', MJItems.TANTALUM_SHEET)
                .unlockedBy("has_tantalum_sheet", has(MJItems.TANTALUM_SHEET))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MJItems.LUCK_UPGRADE.get())
                .pattern("RLR")
                .pattern("LPL")
                .pattern("RLR")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('P', MJItems.TANTALUM_SHEET)
                .unlockedBy("has_tantalum_sheet", has(MJItems.TANTALUM_SHEET))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MJItems.TINTED_PLANET_CARD.get())
                .pattern(" D ")
                .pattern("DPD")
                .pattern(" D ")
                .define('D', Tags.Items.DYES)
                .define('P', MJItems.PLANET_CARD)
                .unlockedBy("has_planet_card", has(MJItems.PLANET_CARD))
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
