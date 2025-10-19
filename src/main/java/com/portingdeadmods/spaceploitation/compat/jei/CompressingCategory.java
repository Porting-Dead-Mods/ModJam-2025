package com.portingdeadmods.spaceploitation.compat.jei;

import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.content.recipe.CompressingRecipe;
import com.portingdeadmods.spaceploitation.registries.MJBlocks;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class CompressingCategory extends AbstractRecipeCategory<CompressingRecipe> {
    public static final RecipeType<CompressingRecipe> TYPE = RecipeType.create(Spaceploitation.MODID, "compressing", CompressingRecipe.class);

    public CompressingCategory(IGuiHelper guiHelper) {
        super(TYPE, Component.translatable("jei.spaceploitation.category.compressing"), guiHelper.createDrawableItemLike(MJBlocks.COMPRESSOR), 82, 54);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompressingRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(1, 19)
                .setStandardSlotBackground()
                .addIngredients(iWCToIngredientSaveCount(recipe.ingredient()));

        builder.addOutputSlot(61, 19)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }


    public static @NotNull Ingredient iWCToIngredientSaveCount(IngredientWithCount ingredientWithCount) {
        Ingredient ingredient = ingredientWithCount.ingredient();
        for (ItemStack itemStack : ingredient.getItems()) {
            itemStack.setCount(ingredientWithCount.count());
        }
        return ingredient;
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, CompressingRecipe recipe, IFocusGroup focuses) {
        builder.addAnimatedRecipeArrow(recipe.duration())
                .setPosition(26, 17);
    }
}
