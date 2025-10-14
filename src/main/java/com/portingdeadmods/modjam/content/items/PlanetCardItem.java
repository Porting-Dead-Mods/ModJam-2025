package com.portingdeadmods.modjam.content.items;

import com.portingdeadmods.modjam.data.PlanetComponent;
import com.portingdeadmods.modjam.data.PlanetType;
import com.portingdeadmods.modjam.registries.MJDataComponents;
import com.portingdeadmods.modjam.registries.MJTranslations;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class PlanetCardItem extends Item {
    public PlanetCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        PlanetComponent currentComponent = stack.getOrDefault(MJDataComponents.PLANET, PlanetComponent.EMPTY);
        
        if (currentComponent.planetType().isPresent()) {
            PlanetType planetType = currentComponent.planetType().get();
            ResourceKey<Level> currentDimension = level.dimension();
            
            if (currentDimension.equals(planetType.dimension())) {
                if (!currentComponent.activated()) {
                    stack.set(MJDataComponents.PLANET, new PlanetComponent(currentComponent.planetType(), true));
                    player.displayClientMessage(Component.literal("Planet Card Activated!").withStyle(ChatFormatting.GREEN), true);
                    return InteractionResultHolder.success(stack);
                } else {
                    player.displayClientMessage(Component.literal("Already activated!").withStyle(ChatFormatting.YELLOW), true);
                    return InteractionResultHolder.pass(stack);
                }
            } else {
                player.displayClientMessage(Component.literal("Wrong dimension!").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        PlanetComponent planetComponent = stack.get(MJDataComponents.PLANET);

        if (planetComponent != null && planetComponent.planetType().isPresent()) {
            PlanetType planetType = planetComponent.planetType().get();
            
            tooltip.add(Component.literal("Dimension: ")
                    .append(Utils.registryTranslation(planetType.dimension()))
                    .withStyle(ChatFormatting.GRAY));
            
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
