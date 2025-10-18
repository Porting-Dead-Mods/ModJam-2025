package com.portingdeadmods.modjam.compat.jei;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.recipe.PlanetSimulatorRecipe;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.placement.IPlaceable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.ITextWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.Arrays;
import java.util.List;

public class PlanetSimulatorCategory extends AbstractRecipeCategory<PlanetSimulatorRecipe> {
    public static final ResourceLocation RECIPE_ARROW_VERTICAL_SPRITE = Modjam.rl("textures/gui/sprites/recipe_arrow_vertical.png");
    public static final ResourceLocation RECIPE_ARROW_VERTICAL_FILLED_SPRITE = Modjam.rl("textures/gui/sprites/recipe_arrow_filled_vertical.png");

    public static final RecipeType<PlanetSimulatorRecipe> TYPE = RecipeType.create(Modjam.MODID, "planet_simulator", PlanetSimulatorRecipe.class);
    @org.jetbrains.annotations.NotNull
    private final IGuiHelper guiHelper;

    public PlanetSimulatorCategory(IGuiHelper guiHelper) {
        super(TYPE, Component.translatable("jei.modjam.category.planet_simulator"), guiHelper.createDrawableItemLike(MJBlocks.PLANET_SIMULATOR_CONTROLLER), 176, 126);
        this.guiHelper = guiHelper;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlanetSimulatorRecipe recipe, IFocusGroup focuses) {
        int size = recipe.inputs().size();
        int startX = (this.getWidth() - size * 18) / 2;

        for (int i = 0; i < size; i++) {
            builder.addInputSlot(startX + i * 18, 10)
                    .setStandardSlotBackground()
                    .addIngredients(CompressingCategory.iWCToIngredientSaveCount(recipe.inputs().get(i)));
        }

        if (recipe.fluidInput().isPresent()) {
            builder.addInputSlot(60, 1)
                    .setStandardSlotBackground()
                    .setFluidRenderer(10000, true, 16, 54)
                    .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidInput().get().getStacks()).toList());
        }

        int outputY = 20;

        for (int i = 0; i < recipe.outputs().size() && i < 9; i++) {
            PlanetSimulatorRecipe.WeightedOutput output = recipe.outputs().get(i);
            int itemsInFirstRow = Math.min(recipe.outputs().size(), 3);
            int outputX = (this.getWidth() - itemsInFirstRow * 18) / 2;
            int slotX = outputX + (i % 3) * 18;
            int slotY = outputY + (i / 3) * 18;

            IRecipeSlotBuilder slot = builder.addOutputSlot(slotX, slotY + 40)
                    .setStandardSlotBackground();

            if (output.itemStack().isPresent()) {
                slot.addItemStack(output.itemStack().get());
            }

            if (output.fluidStack().isPresent()) {
                slot.setFluidRenderer(10000, false, 16, 16)
                        .addIngredient(NeoForgeTypes.FLUID_STACK, output.fluidStack().get());
            }

            if (output.chance() < 1.0f) {
                slot.addRichTooltipCallback((view, tooltip) -> {
                    tooltip.add(Component.literal("Chance: " + (int) (output.chance() * 100) + "%"));
                });
            }
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, PlanetSimulatorRecipe recipe, IFocusGroup focuses) {
        IDrawableStatic recipeArrow = this.guiHelper.drawableBuilder(RECIPE_ARROW_VERTICAL_SPRITE, 0, 0, 16, 24)
                .setTextureSize(16, 24)
                .build();
        IDrawableStatic recipeArrowFilled = this.guiHelper.drawableBuilder(RECIPE_ARROW_VERTICAL_FILLED_SPRITE, 0, 0, 16, 24)
                .setTextureSize(16, 24)
                .build();
        IDrawableAnimated animatedDrawable = this.guiHelper.createAnimatedDrawable(recipeArrowFilled, recipe.duration(), IDrawableAnimated.StartDirection.TOP, false);

        builder.addDrawable(recipeArrow).setPosition(this.getWidth() / 2 - 8, this.getHeight() / 2 - 12 - 20);
        builder.addDrawable(animatedDrawable).setPosition(this.getWidth() / 2 - 8, this.getHeight() / 2 - 12 - 20);
        MutableComponent literal = Component.literal(recipe.energyPerTick() + " FE/t");
        Font font = Minecraft.getInstance().font;
        addText(builder, literal
                .withStyle(ChatFormatting.DARK_GRAY))
                .setPosition(this.getWidth() - font.width(literal), this.getHeight() - font.lineHeight * 2 - 2 - 8);
        addText(builder, Component.literal(recipe.duration() + " ticks")
                .withStyle(ChatFormatting.DARK_GRAY))
                .setPosition(this.getWidth() - font.width(literal), this.getHeight() - font.lineHeight - 8);
    }

    private static ITextWidget addText(IRecipeExtrasBuilder builder, MutableComponent component) {
        return builder.addText(component, Minecraft.getInstance().font.width(component), Minecraft.getInstance().font.lineHeight);
    }

}
