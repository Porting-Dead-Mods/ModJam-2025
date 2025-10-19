package com.portingdeadmods.spaceploitation.compat.jei;

import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.content.recipe.PlanetPowerRecipe;
import com.portingdeadmods.spaceploitation.data.PlanetComponent;
import com.portingdeadmods.spaceploitation.data.PlanetType;
import com.portingdeadmods.spaceploitation.registries.MJBlocks;
import com.portingdeadmods.spaceploitation.registries.MJDataComponents;
import com.portingdeadmods.spaceploitation.registries.MJItems;
import com.portingdeadmods.spaceploitation.registries.MJRegistries;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.ITextWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Optional;

public class PlanetPowerCategory extends AbstractRecipeCategory<PlanetPowerRecipe> {
    public static final RecipeType<PlanetPowerRecipe> TYPE = RecipeType.create(Spaceploitation.MODID, "planet_power", PlanetPowerRecipe.class);
    private final IPlatformFluidHelper<?> fluidHelper;

    public PlanetPowerCategory(IGuiHelper guiHelper, IPlatformFluidHelper<?> fluidHelper) {
        super(TYPE, Component.translatable("jei.spaceploitation.category.planet_power"), guiHelper.createDrawableItemLike(MJBlocks.PLANET_SIMULATOR_CONTROLLER), 176, 90);
        this.fluidHelper = fluidHelper;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlanetPowerRecipe recipe, IFocusGroup focuses) {
        ItemStack planetCard = createPlanetCard(recipe);
        if (!planetCard.isEmpty()) {
            builder.addInputSlot(this.getWidth() - 18, 1)
                    .setStandardSlotBackground()
                    .addItemStack(planetCard);
        }

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

    private ItemStack createPlanetCard(PlanetPowerRecipe recipe) {
        var holderOpt = Minecraft.getInstance().level.registryAccess()
                .lookupOrThrow(MJRegistries.PLANET_TYPE_KEY)
                .get(recipe.planetType());

        if (holderOpt.isEmpty()) {
            return ItemStack.EMPTY;
        }

        PlanetType planetType = holderOpt.get().value();

        Item itemToUse = planetType.tint().isPresent() ? MJItems.TINTED_PLANET_CARD.get() : MJItems.PLANET_CARD.get();
        ItemStack stack = new ItemStack(itemToUse);
        stack.set(MJDataComponents.PLANET, new PlanetComponent(Optional.of(planetType), true, planetType.isBlackHole()));

        return stack;
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, PlanetPowerRecipe recipe, IFocusGroup focuses) {
        Font font = Minecraft.getInstance().font;

        MutableComponent generates = Component.literal("Generates: " + recipe.energyPerTick() + " FE/t");
        addText(builder, generates)
                .setPosition((this.getWidth() - font.width(generates)) / 2, this.getHeight() / 2 - font.lineHeight);
        MutableComponent duration = Component.literal("Duration: " + recipe.duration() + " ticks");
        addText(builder, duration)
                .setPosition((this.getWidth() - font.width(duration)) / 2, this.getHeight() / 2);
    }

    private ITextWidget addText(IRecipeExtrasBuilder builder, MutableComponent component) {
        return builder.addText(component, Minecraft.getInstance().font.width(component), Minecraft.getInstance().font.lineHeight);
    }
}
