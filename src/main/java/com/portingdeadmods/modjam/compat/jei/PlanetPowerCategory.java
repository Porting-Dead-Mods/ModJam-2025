package com.portingdeadmods.modjam.compat.jei;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.recipe.PlanetPowerRecipe;
import com.portingdeadmods.modjam.registries.MJBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PlanetPowerCategory extends AbstractRecipeCategory<PlanetPowerRecipe> {
    public static final RecipeType<PlanetPowerRecipe> TYPE = RecipeType.create(Modjam.MODID, "planet_power", PlanetPowerRecipe.class);
    private final IPlatformFluidHelper<?> fluidHelper;

    public PlanetPowerCategory(IGuiHelper guiHelper, IPlatformFluidHelper<?> fluidHelper) {
        super(TYPE, Component.translatable("jei.modjam.category.planet_power"), guiHelper.createDrawableItemLike(MJBlocks.PLANET_SIMULATOR_CONTROLLER), 176, 90);
        this.fluidHelper = fluidHelper;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlanetPowerRecipe recipe, IFocusGroup focuses) {
        int inputX = 1;
        int inputY = 1;
        
        for (int i = 0; i < recipe.catalysts().size() && i < 9; i++) {
            int slotX = inputX + (i % 3) * 18;
            int slotY = inputY + (i / 3) * 18;
            builder.addInputSlot(slotX, slotY)
                    .setStandardSlotBackground()
                    .addIngredients(CompressingCategory.iWCToIngredientSaveCount(recipe.catalysts().get(i)));
        }
        
        if (recipe.fluidInput().isPresent()) {
            builder.addInputSlot(60, 1)
                    .setStandardSlotBackground()
                    .setFluidRenderer(10000, true, 16, 54)
                    .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidInput().get().getStacks()).toList());
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, PlanetPowerRecipe recipe, IFocusGroup focuses) {
        builder.addText(Component.literal("Generates: " + recipe.energyPerTick() + " FE/t"), 1, 60);
        builder.addText(Component.literal("Duration: " + recipe.duration() + " ticks"), 1, 70);
    }
}
