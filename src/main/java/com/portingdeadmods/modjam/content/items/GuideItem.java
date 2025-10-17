package com.portingdeadmods.modjam.content.items;

import com.portingdeadmods.modjam.compat.guideme.GuideMECompat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

public class GuideItem extends Item {
    public GuideItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (level.isClientSide && ModList.get().isLoaded("guideme")) {
            GuideMECompat.openGuide(player);
        }
        
        return InteractionResultHolder.success(itemStack);
    }
}
