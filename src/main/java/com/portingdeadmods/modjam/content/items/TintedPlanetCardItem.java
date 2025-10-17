package com.portingdeadmods.modjam.content.items;

import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.modjam.registries.MJDataComponents;
import com.portingdeadmods.modjam.registries.MJPlanetCards;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class TintedPlanetCardItem extends PlanetCardItem {
    public TintedPlanetCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        PlanetComponent currentComponent = stack.getOrDefault(MJDataComponents.PLANET, PlanetComponent.EMPTY);
        
        if (currentComponent.planetType().isPresent()) {
            return super.use(level, player, usedHand);
        } else {
            Optional<PlanetType> matchingPlanet = findMatchingPlanetTypeTinted(level);
            
            if (matchingPlanet.isPresent()) {
                stack.set(MJDataComponents.PLANET, new PlanetComponent(matchingPlanet, false));
                player.displayClientMessage(Component.literal("Planet Card linked!").withStyle(ChatFormatting.AQUA), true);
                return InteractionResultHolder.success(stack);
            } else {
                player.displayClientMessage(Component.literal("No matching planet for this dimension!").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }
        }
    }
    
    private Optional<PlanetType> findMatchingPlanetTypeTinted(Level level) {
        ResourceKey<Level> currentDimension = level.dimension();
        
        for (PlanetType planetType : MJPlanetCards.getAllPlanetTypes()) {
            if (planetType.dimension().equals(currentDimension)) {
                return Optional.of(planetType);
            }
        }
        
        return Optional.empty();
    }
}
