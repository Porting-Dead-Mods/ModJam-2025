package com.portingdeadmods.modjam.content.items;

import com.portingdeadmods.modjam.data.UpgradeType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class UpgradeItem extends Item {
    private final UpgradeType upgradeType;

    public UpgradeItem(UpgradeType upgradeType, Properties properties) {
        super(properties);
        this.upgradeType = upgradeType;
    }

    public UpgradeType getUpgradeType() {
        return upgradeType;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.modjam.upgrade." + upgradeType.getName()).withStyle(ChatFormatting.GRAY));
    }
}
