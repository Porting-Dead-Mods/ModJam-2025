package com.portingdeadmods.modjam.content.items;

import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.modjam.registries.MJDataComponents;
import com.portingdeadmods.modjam.registries.MJItems;
import com.portingdeadmods.modjam.registries.MJPlanetCards;
import com.portingdeadmods.modjam.registries.MJTranslations;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class PlanetCardItem extends Item {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetCardItem.class);
    
    public PlanetCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        PlanetComponent currentComponent = stack.getOrDefault(MJDataComponents.PLANET, PlanetComponent.EMPTY);
        
        if (currentComponent.planetType().isPresent()) {
            PlanetType planetType = currentComponent.planetType().get();
            
            if (matchesPlanetType(level, player, planetType)) {
                if (!currentComponent.activated()) {
                    stack.set(MJDataComponents.PLANET, new PlanetComponent(currentComponent.planetType(), true, currentComponent.isBlackHole()));
                    player.displayClientMessage(Component.literal("Planet Card Activated!").withStyle(ChatFormatting.GREEN), true);
                    return InteractionResultHolder.success(stack);
                } else {
                    player.displayClientMessage(Component.literal("Already activated!").withStyle(ChatFormatting.YELLOW), true);
                    return InteractionResultHolder.pass(stack);
                }
            } else {
                player.displayClientMessage(Component.literal("Wrong location!").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }
        } else {
            Optional<PlanetType> matchingPlanet = findMatchingPlanetType(level, player);
            
            if (matchingPlanet.isPresent()) {
                PlanetType planetType = matchingPlanet.get();
                ItemStack resultStack = stack;
                
                if (planetType.tint().isPresent() && !(this instanceof TintedPlanetCardItem)) {
                    resultStack = new ItemStack(MJItems.TINTED_PLANET_CARD.get());
                }
                
                resultStack.set(MJDataComponents.PLANET, new PlanetComponent(matchingPlanet, false, planetType.isBlackHole()));
                player.displayClientMessage(Component.literal("Planet Card linked!").withStyle(ChatFormatting.AQUA), true);
                
                if (resultStack != stack) {
                    player.setItemInHand(usedHand, resultStack);
                }
                
                return InteractionResultHolder.success(resultStack);
            } else {
                player.displayClientMessage(Component.literal("No matching planet for this location!").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }
        }
    }
    
    protected boolean matchesPlanetType(Level level, Player player, PlanetType planetType) {
        if (planetType.structure().isPresent() && level instanceof ServerLevel serverLevel) {
            var structureRegistry = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE);
            var structureHolder = structureRegistry.getHolder(planetType.structure().get());
            if (structureHolder.isPresent()) {
                return serverLevel.structureManager().getStructureWithPieceAt(player.blockPosition(), structureHolder.get().value()).isValid();
            }
        }
        
        if (planetType.biome().isPresent()) {
            ResourceKey<Biome> currentBiome = level.getBiome(player.blockPosition()).unwrapKey().orElse(null);
            return currentBiome != null && currentBiome.equals(planetType.biome().get());
        }
        
        if (planetType.dimension().isPresent()) {
            return level.dimension().equals(planetType.dimension().get());
        }
        
        return false;
    }
    
    protected Optional<PlanetType> findMatchingPlanetType(Level level, Player player) {
        LOGGER.info("Finding matching planet type at {} for player {}", player.blockPosition(), player.getName().getString());
        
        if (level instanceof ServerLevel serverLevel) {
            var structureRegistry = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE);
            LOGGER.info("Checking structure-based cards...");
            for (PlanetType planetType : MJPlanetCards.getAllPlanetTypes()) {
                if (planetType.structure().isPresent()) {
                    LOGGER.info("  Checking structure: {}", planetType.structure().get().location());
                    var structureHolder = structureRegistry.getHolder(planetType.structure().get());
                    if (structureHolder.isPresent() && serverLevel.structureManager().getStructureWithPieceAt(player.blockPosition(), structureHolder.get().value()).isValid()) {
                        LOGGER.info("    -> MATCHED structure card: {}", planetType.texture());
                        return Optional.of(planetType);
                    }
                }
            }
        }
        
        ResourceKey<Biome> currentBiome = level.getBiome(player.blockPosition()).unwrapKey().orElse(null);
        LOGGER.info("Checking biome-based cards... Current biome: {}", currentBiome != null ? currentBiome.location() : "null");
        for (PlanetType planetType : MJPlanetCards.getAllPlanetTypes()) {
            if (planetType.biome().isPresent()) {
                LOGGER.info("  Checking biome card: {} (has biome: {})", planetType.texture(), planetType.biome().get().location());
                if (currentBiome != null && currentBiome.equals(planetType.biome().get())) {
                    LOGGER.info("    -> MATCHED biome card: {}", planetType.texture());
                    return Optional.of(planetType);
                }
            }
        }
        
        ResourceKey<Level> currentDimension = level.dimension();
        LOGGER.info("Checking dimension-only cards... Current dimension: {}", currentDimension.location());
        for (PlanetType planetType : MJPlanetCards.getAllPlanetTypes()) {
            if (planetType.dimension().isPresent() 
                && planetType.dimension().get().equals(currentDimension)
                && planetType.structure().isEmpty()
                && planetType.biome().isEmpty()) {
                LOGGER.info("  Checking dimension-only card: {} (dimension: {})", planetType.texture(), planetType.dimension().get().location());
                LOGGER.info("    -> MATCHED dimension-only card: {}", planetType.texture());
                return Optional.of(planetType);
            }
        }
        
        LOGGER.info("Checking fallback dimension cards...");
        for (PlanetType planetType : MJPlanetCards.getAllPlanetTypes()) {
            if (planetType.dimension().isPresent() && planetType.dimension().get().equals(currentDimension)) {
                LOGGER.info("  -> MATCHED fallback dimension card: {}", planetType.texture());
                return Optional.of(planetType);
            }
        }
        
        LOGGER.info("No matching planet card found!");
        return Optional.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        PlanetComponent planetComponent = stack.get(MJDataComponents.PLANET);

        if (planetComponent != null && planetComponent.planetType().isPresent()) {
            PlanetType planetType = planetComponent.planetType().get();
            
            if (planetType.structure().isPresent()) {
                tooltip.add(Component.literal("Structure: ")
                        .append(Utils.registryTranslation(planetType.structure().get()))
                        .withStyle(ChatFormatting.GRAY));
            } else if (planetType.biome().isPresent()) {
                tooltip.add(Component.literal("Biome: ")
                        .append(Utils.registryTranslation(planetType.biome().get()))
                        .withStyle(ChatFormatting.GRAY));
            } else if (planetType.dimension().isPresent()) {
                tooltip.add(Component.literal("Dimension: ")
                        .append(Utils.registryTranslation(planetType.dimension().get()))
                        .withStyle(ChatFormatting.GRAY));
            }
            
            tooltip.add(Component.literal("Status: ")
                    .append(Component.literal(planetComponent.activated() ? "Activated" : "Not Activated")
                            .withStyle(planetComponent.activated() ? ChatFormatting.GREEN : ChatFormatting.RED))
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
