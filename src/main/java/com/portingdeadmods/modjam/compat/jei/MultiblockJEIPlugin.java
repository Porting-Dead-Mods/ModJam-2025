package com.portingdeadmods.modjam.compat.jei;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.client.screens.CompressorScreen;
import com.portingdeadmods.modjam.content.recipe.CompressingRecipe;
import com.portingdeadmods.modjam.content.recipe.PlanetPowerRecipe;
import com.portingdeadmods.modjam.content.recipe.PlanetSimulatorRecipe;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.modjam.registries.MJItems;
import com.portingdeadmods.portingdeadlibs.api.client.screens.PanelContainerScreen;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@JeiPlugin
public class MultiblockJEIPlugin implements IModPlugin {
    
    private static final List<Supplier<Multiblock>> MULTIBLOCKS = new ArrayList<>();
    
    public static void registerMultiblock(Supplier<? extends Multiblock> multiblock) {
        MULTIBLOCKS.add((Supplier<Multiblock>) multiblock);
    }
    
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Modjam.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MultiblockJEICategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CompressingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PlanetSimulatorCategory(registration.getJeiHelpers().getGuiHelper(), registration.getJeiHelpers().getPlatformFluidHelper()));
        registration.addRecipeCategories(new PlanetPowerCategory(registration.getJeiHelpers().getGuiHelper(), registration.getJeiHelpers().getPlatformFluidHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(MultiblockJEICategory.RECIPE_TYPE, MULTIBLOCKS.stream().map(Supplier::get).toList());

        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<CompressingRecipe> compressingRecipes = recipeManager.getAllRecipesFor(CompressingRecipe.TYPE)
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(CompressingCategory.TYPE, compressingRecipes);

        List<PlanetSimulatorRecipe> planetSimulatorRecipes = recipeManager.getAllRecipesFor(PlanetSimulatorRecipe.TYPE)
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(PlanetSimulatorCategory.TYPE, planetSimulatorRecipes);

        List<PlanetPowerRecipe> planetPowerRecipes = recipeManager.getAllRecipesFor(PlanetPowerRecipe.TYPE)
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(PlanetPowerCategory.TYPE, planetPowerRecipes);

//        ItemStack dustStack = new ItemStack(MJItems.TANTALUM_DUST.get());
//        dustStack.set(DataComponents.CUSTOM_NAME, Component.translatable("modjam.jei.grinding_output").withStyle(ChatFormatting.RESET));
//
//        ItemStack oreStack = new ItemStack(Items.COBBLESTONE);
//        oreStack.set(DataComponents.CUSTOM_NAME, Component.translatable("modjam.jei.grinding_input").withStyle(ChatFormatting.RESET));
//
//        registration.addRecipes(RecipeTypes.CRAFTING, Collections.singletonList(new RecipeHolder<>(
//                ResourceLocation.fromNamespaceAndPath(Modjam.MODID, "grinding_example"),
//                new ShapelessRecipe(
//                        "grinding",
//                        CraftingBookCategory.MISC,
//                        dustStack,
//                        NonNullList.of(Ingredient.EMPTY,
//                                Ingredient.of(MJItems.PESTLE.get()),
//                                Ingredient.of(oreStack),
//                                Ingredient.of(MJItems.MORTAR.get()))
//                )
//        )));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (Supplier<Multiblock> multiblock : MULTIBLOCKS) {
            registration.addRecipeCatalyst(
                new ItemStack(multiblock.get().getUnformedController()),
                MultiblockJEICategory.RECIPE_TYPE
            );
        }
        
        registration.addRecipeCatalyst(new ItemStack(MJBlocks.COMPRESSOR.get()), CompressingCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(MJBlocks.PLANET_SIMULATOR_CONTROLLER.get()), PlanetSimulatorCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(MJBlocks.PLANET_SIMULATOR_CONTROLLER.get()), PlanetPowerCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(PanelContainerScreen.class, new IGuiContainerHandler<PanelContainerScreen<?>>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(PanelContainerScreen<?> containerScreen) {
                return containerScreen.getBounds();
            }
        });
    }
}
