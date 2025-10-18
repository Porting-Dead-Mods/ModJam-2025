package com.portingdeadmods.modjam.compat.jei;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.recipe.PlanetSimulatorRecipe;
import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.modjam.registries.MJDataComponents;
import com.portingdeadmods.modjam.registries.MJItems;
import com.portingdeadmods.modjam.registries.MJRegistries;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.placement.IPlaceable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
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
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        int totalSlots = recipe.catalysts().size() + recipe.inputs().size();
        int startX = (this.getWidth() - totalSlots * 18) / 2;
        
        int slotIndex = 0;
        for (int i = 0; i < recipe.catalysts().size(); i++) {
            builder.addInputSlot(startX + slotIndex * 18, 10)
                    .setStandardSlotBackground()
                    .addIngredients(CompressingCategory.iWCToIngredientSaveCount(recipe.catalysts().get(i)))
                    .addRichTooltipCallback((view, tooltip) -> {
                        tooltip.add(Component.literal("Catalyst (Not consumed)").withStyle(ChatFormatting.AQUA));
                    });
            slotIndex++;
        }

        for (int i = 0; i < recipe.inputs().size(); i++) {
            builder.addInputSlot(startX + slotIndex * 18, 10)
                    .setStandardSlotBackground()
                    .addIngredients(CompressingCategory.iWCToIngredientSaveCount(recipe.inputs().get(i)));
            slotIndex++;
        }

        if (recipe.fluidInput().isPresent()) {
            builder.addInputSlot(60, 1)
                    .setStandardSlotBackground()
                    .setFluidRenderer(10000, true, 16, 54)
                    .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.fluidInput().get().getStacks()).toList());
        }

        ItemStack planetCard = createPlanetCard(recipe);
        if (!planetCard.isEmpty()) {
            builder.addInputSlot(this.getWidth() - 18, 1)
                    .setStandardSlotBackground()
                    .addItemStack(planetCard);
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
                    float percentage = output.chance() * 100;
                    String formatted;
                    if (percentage >= 10) {
                        formatted = String.format("%.1f%%", percentage);
                    } else if (percentage >= 1) {
                        formatted = String.format("%.2f%%", percentage);
                    } else {
                        formatted = String.format("%.3f%%", percentage);
                    }
                    tooltip.add(Component.literal("Chance: " + formatted).withStyle(ChatFormatting.GOLD));
                });
            }
        }
    }

    private ItemStack createPlanetCard(PlanetSimulatorRecipe recipe) {
        var holderOpt = Minecraft.getInstance().level.registryAccess()
                .lookupOrThrow(MJRegistries.PLANET_TYPE_KEY)
                .get(recipe.planetType());
        
        if (holderOpt.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        PlanetType planetType = holderOpt.get().value();
        
        Item itemToUse = planetType.tint().isPresent() ? MJItems.TINTED_PLANET_CARD.get() : MJItems.PLANET_CARD.get();
        ItemStack stack = new ItemStack(itemToUse);
        stack.set(MJDataComponents.PLANET, new PlanetComponent(Optional.of(planetType), true));
        
        return stack;
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

        int arrowX = this.getWidth() / 2 - 8;
        int arrowY = this.getHeight() / 2 - 12 - 20;
        
        builder.addDrawable(recipeArrow).setPosition(arrowX, arrowY);
        builder.addDrawable(animatedDrawable).setPosition(arrowX, arrowY);
        
        builder.addWidget(new IRecipeWidget() {
            @Override
            public ScreenPosition getPosition() {
                return new ScreenPosition(arrowX, arrowY);
            }
            
            @Override
            public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
                if (mouseX >= 0 && mouseX < 16 && mouseY >= 0 && mouseY < 24) {
                    int durationTicks = recipe.duration();
                    float durationSeconds = durationTicks / 20.0f;
                    int totalEnergy = recipe.energyPerTick() * durationTicks;
                    
                    if (durationSeconds < 60) {
                        tooltip.add(Component.literal(String.format("Time: %.1f seconds", durationSeconds)).withStyle(ChatFormatting.GRAY));
                    } else {
                        int minutes = (int) (durationSeconds / 60);
                        float seconds = durationSeconds % 60;
                        if (seconds > 0.05f) {
                            tooltip.add(Component.literal(String.format("Time: %d min %.1f sec", minutes, seconds)).withStyle(ChatFormatting.GRAY));
                        } else {
                            tooltip.add(Component.literal(String.format("Time: %d min", minutes)).withStyle(ChatFormatting.GRAY));
                        }
                    }
                    
                    String energyFormatted;
                    if (totalEnergy >= 1_000_000) {
                        energyFormatted = String.format("%.2f M FE", totalEnergy / 1_000_000.0);
                    } else if (totalEnergy >= 1_000) {
                        energyFormatted = String.format("%.1f K FE", totalEnergy / 1_000.0);
                    } else {
                        energyFormatted = String.format("%,d FE", totalEnergy);
                    }
                    tooltip.add(Component.literal("Total Energy: " + energyFormatted).withStyle(ChatFormatting.AQUA));
                }
            }
        });
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
