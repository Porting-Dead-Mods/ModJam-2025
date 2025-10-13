package com.portingdeadmods.modjam.content.items;

import com.portingdeadmods.modjam.data.PlanetComponent;
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
        ResourceKey<Level> dimension = level.dimension();
        ItemStack stack = player.getItemInHand(usedHand);
        stack.set(MJDataComponents.PLANET, new PlanetComponent(Optional.of(dimension)));

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext p_339594_, List<Component> tooltip, TooltipFlag p_41424_) {
        PlanetComponent planetComponent = stack.get(MJDataComponents.PLANET);

        Optional<ResourceKey<Level>> dimension = planetComponent.dimension();
        tooltip.add(MJTranslations.PLANET_CARD.component(dimension
                        .map(key -> Utils.registryTranslation(key).getString())
                        .orElse("Empty"))
                .withStyle(ChatFormatting.GRAY));

    }

}
