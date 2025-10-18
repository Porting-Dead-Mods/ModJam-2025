package com.portingdeadmods.modjam.datagen.recipe;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.modjam.datagen.recipe.builder.PlanetSimulatorRecipeBuilder;
import com.portingdeadmods.modjam.registries.MJRegistries;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public class MJPlanetSimProvider extends RecipeProvider {
    ResourceKey<PlanetType> earth = createKey("earth");
    ResourceKey<PlanetType> mars = createKey("mars");
    ResourceKey<PlanetType> venus = createKey("venus");
    ResourceKey<PlanetType> blackHole = createKey("blackhole");

    private ResourceKey<PlanetType> createKey(String path) {
        return ResourceKey.create(MJRegistries.PLANET_TYPE_KEY, Modjam.rl(path));
    }
    public MJPlanetSimProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public final String getName() {
        return "Planet Simulator";
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput, HolderLookup.Provider holderLookup) {
        earth(pRecipeOutput, holderLookup);
        mars(pRecipeOutput, holderLookup);
        venus(pRecipeOutput, holderLookup);
        blackHole(pRecipeOutput, holderLookup);
    }

    private void earth(RecipeOutput pRecipeOutput, HolderLookup.Provider holderLookup) {
        // Resource Generation
        simple("generation/coal", earth, 800, 120)
                .input(Items.COBBLESTONE, 64)
                .result(Items.COAL, 0.02f)
                .result(Items.IRON_ORE, 0.015f)
                .result(Items.COPPER_ORE, 0.018f)
                .save(pRecipeOutput);
        simple("generation/diamond", earth, 2400, 500)
                .input(Items.DEEPSLATE, 64)
                .result(Items.DIAMOND, 0.008f)
                .result(Items.EMERALD, 0.005f)
                .result(Items.REDSTONE, 0.02f)
                .result(Items.LAPIS_LAZULI, 0.015f)
                .save(pRecipeOutput);
        simple("generation/gold", earth, 1200, 200)
                .input(Items.DEEPSLATE, 64)
                .result(Items.RAW_GOLD, 0.012f)
                .result(Items.REDSTONE, 0.025f)
                .result(Items.LAPIS_LAZULI, 0.018f)
                .result(Items.RAW_IRON, 0.02f)
                .save(pRecipeOutput);
        simple("generation/iron", earth, 1000, 150)
                .input(Items.STONE, 64)
                .result(Items.RAW_IRON, 0.025f)
                .result(Items.RAW_COPPER, 0.02f)
                .result(Items.COAL, 0.03f)
                .save(pRecipeOutput);
        // Mob Drops
        simple("mob_drops/misc_hostile", earth, 1000, 180)
                .input(Items.DIRT, 64)
                .result(Items.ROTTEN_FLESH, 0.025f)
                .result(Items.BONE, 0.02f)
                .result(Items.GUNPOWDER, 0.01f)
                .result(Items.STRING, 0.015f)
                .result(Items.SPIDER_EYE, 0.008f)
                .result(Items.SLIME_BALL, 0.005f)
                .save(pRecipeOutput);
        simple("mob_drops/undead", earth, 3800, 800)
                .input(Items.DIRT, 64)
                .result(Items.ROTTEN_FLESH, 0.025f)
                .result(Items.BONE, 0.02f)
                .result(Items.IRON_INGOT, 0.001f)
                .result(Items.COPPER_INGOT, 0.001f)
                .result(Items.ARROW, 0.001f)
                .result(Items.ARROW, 3, 0.0008f)
                .result(tippedArrow(Potions.SLOWNESS, 1), 0.001f)
                .result(tippedArrow(Potions.SLOWNESS, 3), 0.0008f)
                .result(tippedArrow(Potions.POISON, 1), 0.001f)
                .result(tippedArrow(Potions.POISON, 3), 0.0008f)
                .result(Items.CARROT, 0.015f)
                .result(Items.POTATO, 0.015f)
                .result(Items.NAUTILUS_SHELL, 0.015f)
                .save(pRecipeOutput);
    }

    private void mars(RecipeOutput pRecipeOutput, HolderLookup.Provider holderLookup) {
        // Resource Generation
        simple("generation/ancient_debris", mars, 3000, 800)
                .input(Items.BASALT, 64)
                .result(Items.ANCIENT_DEBRIS, 0.002f)
                .result(Items.NETHERITE_SCRAP, 0.001f)
                .result(Items.BLACKSTONE, 0.025f)
                .save(pRecipeOutput);
        simple("generation/glowstone", mars, 800, 150)
                .input(Items.NETHERRACK, 64)
                .result(Items.GLOWSTONE_DUST, 0.022f)
                .result(Items.GLOWSTONE_DUST, 2, 0.015f)
                .result(Items.GLOWSTONE_DUST, 4, 0.011f)
                .result(Items.GLOWSTONE, 0.005f)
                .save(pRecipeOutput);
        simple("generation/gold", mars, 900, 160)
                .input(Items.NETHERRACK, 64)
                .result(Items.GOLD_NUGGET, 0.02f)
                .result(Items.NETHER_GOLD_ORE, 0.008f)
                .save(pRecipeOutput);
        simple("generation/nether_quartz", mars, 800, 140)
                .input(Items.NETHERRACK, 64)
                .result(Items.QUARTZ, 0.025f)
                .result(Items.GLOWSTONE_DUST, 2, 0.015f)
                .result(Items.GLOWSTONE_DUST, 4, 0.011f)
                .result(Items.GOLD_NUGGET, 0.018f)
                .save(pRecipeOutput);
        // Mob Drops
        simple("mob_drops/blaze", mars, 1200, 250)
                .input(Items.NETHERRACK, 64)
                .result(Items.BLAZE_ROD, 0.008f)
                .result(Items.BLAZE_POWDER, 0.015f)
                .result(Items.MAGMA_CREAM, 0.012f)
                .save(pRecipeOutput);
        simple("mob_drops/wither_skeleton", mars, 4000, 1200)
                .input(Items.SOUL_SAND, 64)
                .result(Items.WITHER_SKELETON_SKULL, 0.008f)
                .result(Items.NETHER_STAR, 0.002f)
                .result(Items.STONE_SWORD, 0.015f)
                .result(Items.BONE, 0.02f)
                .save(pRecipeOutput);
    }

    private void venus(RecipeOutput pRecipeOutput, HolderLookup.Provider holderLookup) {
        // Resource Generation
        simple("generation/chorus_fruit", venus, 700, 120)
                .input(Items.END_STONE, 64)
                .result(Items.CHORUS_FRUIT, 0.025f)
                .result(Items.CHORUS_FRUIT, 2, 0.0125f)
                .result(Items.CHORUS_FLOWER, 0.008f)
                .save(pRecipeOutput);
        // Resource Refining
        simple("refining/end_stone", venus, 1000, 180)
                .input(Items.END_STONE, 64)
                .result(Items.CHORUS_FRUIT, 0.02f)
                .result(Items.ENDER_PEARL, 0.012f)
                .result(Items.OBSIDIAN, 0.015f)
                .save(pRecipeOutput);
        // Mob Drops
        simple("mob_drops/ender_dragon", venus, 2400, 600)
                .input(Items.END_STONE, 64)
                .result(Items.DRAGON_HEAD, 0.001f)
                .result(Items.DRAGON_EGG, 0.0005f)
                .save(pRecipeOutput);
        simple("mob_drops/dragons_breath", venus, 2400, 800)
                .input(Items.END_STONE, 64)
                .input(Items.GLASS_BOTTLE, 16)
                .result(Items.DRAGON_BREATH, 4, 0.003f)
                .result(Items.DRAGON_BREATH, 3, 0.006f)
                .result(Items.DRAGON_BREATH, 2, 0.012f)
                .result(Items.DRAGON_BREATH, 1, 0.024f)
                .save(pRecipeOutput);
        simple("mob_drops/enderman", venus, 1200, 220)
                .input(Items.END_STONE, 64)
                .result(Items.ENDER_PEARL, 0.018f)
                .result(Items.ENDER_EYE, 0.005f)
                .result(Items.GRASS_BLOCK, 0.0001f)
                .save(pRecipeOutput);
        simple("mob_drops/shulker", venus, 1800, 400)
                .input(Items.PURPUR_BLOCK, 64)
                .result(Items.SHULKER_SHELL, 0.006f)
                .result(Items.SHULKER_BOX, 0.0003f)
                .result(Items.CHORUS_FRUIT, 0.02f)
                .save(pRecipeOutput);
        // Loot Duplication
        simple("loot_duplication/elytra", venus, 6000, 2000)
                .catalyst(Items.ELYTRA, 1)
                .input(Items.END_STONE, 64)
                .result(Items.ELYTRA, 0.0003f)
                .result(Items.PHANTOM_MEMBRANE, 0.008f)
                .save(pRecipeOutput);
    }

    private void blackHole(RecipeOutput pRecipeOutput, HolderLookup.Provider holderLookup) {

    }

    private PlanetSimulatorRecipeBuilder simple(String path, ResourceKey<PlanetType> planetType, int duration, int energy) {
        return PlanetSimulatorRecipeBuilder.recipe(path, planetType)
                .setDuration(duration).setEnergyConsumption(energy);
    }

    private ItemStack tippedArrow(Holder<Potion> potion, int count) {
        ItemStack stack = new ItemStack(Items.TIPPED_ARROW, count);
        stack.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, potion, PotionContents::withPotion);
        return stack;
    }

    private ItemStack enchantRandomly(ItemStack stack, HolderLookup.Provider lookupProvider) {
        RandomSource randomsource = RandomSource.create();
        Stream<Holder.Reference<Enchantment>> stream = lookupProvider.lookupOrThrow(Registries.ENCHANTMENT).listElements().filter(stack::supportsEnchantment);
        List<Holder.Reference<Enchantment>> list = stream.toList();
        Optional<Holder.Reference<Enchantment>> optional = Util.getRandomSafe(list, randomsource);
        if (optional.isPresent()) {
            int i = Mth.nextInt(randomsource, optional.get().value().getMinLevel(), optional.get().value().getMaxLevel());
            stack.enchant(optional.get(), i);
        }
        return stack;
    }
}
