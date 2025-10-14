package com.portingdeadmods.modjam.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.modjam.registries.MJRegistries;
import com.portingdeadmods.portingdeadlibs.api.recipes.IngredientWithCount;
import com.portingdeadmods.portingdeadlibs.api.recipes.PDLRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record PlanetPowerRecipe(
        ResourceKey<PlanetType> planetType,
        List<IngredientWithCount> catalysts,
        Optional<FluidIngredient> fluidInput,
        int energyPerTick,
        int duration
) implements PDLRecipe<SingleRecipeInput> {
    public static final RecipeType<PlanetPowerRecipe> TYPE = RecipeType.simple(Modjam.rl("planet_power"));

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }

    public static final class Serializer implements RecipeSerializer<PlanetPowerRecipe> {
        public static final MapCodec<PlanetPowerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ResourceKey.codec(MJRegistries.PLANET_TYPE_KEY).fieldOf("planet_type").forGetter(PlanetPowerRecipe::planetType),
                IngredientWithCount.CODEC.listOf().optionalFieldOf("catalysts", List.of()).forGetter(PlanetPowerRecipe::catalysts),
                FluidIngredient.CODEC.optionalFieldOf("fluid_input").forGetter(PlanetPowerRecipe::fluidInput),
                Codec.INT.fieldOf("energy_per_tick").forGetter(PlanetPowerRecipe::energyPerTick),
                Codec.INT.fieldOf("duration").forGetter(PlanetPowerRecipe::duration)
        ).apply(inst, PlanetPowerRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PlanetPowerRecipe> STREAM_CODEC = StreamCodec.composite(
                ResourceKey.streamCodec(MJRegistries.PLANET_TYPE_KEY),
                PlanetPowerRecipe::planetType,
                IngredientWithCount.STREAM_CODEC.apply(ByteBufCodecs.list()),
                PlanetPowerRecipe::catalysts,
                ByteBufCodecs.optional(FluidIngredient.STREAM_CODEC),
                PlanetPowerRecipe::fluidInput,
                ByteBufCodecs.INT,
                PlanetPowerRecipe::energyPerTick,
                ByteBufCodecs.INT,
                PlanetPowerRecipe::duration,
                PlanetPowerRecipe::new
        );

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<PlanetPowerRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, PlanetPowerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
