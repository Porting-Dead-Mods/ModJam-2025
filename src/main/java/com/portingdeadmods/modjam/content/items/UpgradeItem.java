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

    public String getFormattedModifier() {
        float value = upgradeType.getModifierValue();
        return switch (upgradeType.getModifierType()) {
            case MULTIPLY -> "x" + String.format(java.util.Locale.US, "%.1f", value);
            case DIVIDE -> "/" + String.format(java.util.Locale.US, "%.1f", value);
            case ADD -> (value >= 0 ? "+" : "") + String.format(java.util.Locale.US, "%.0f%%", value * 100);
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.modjam.upgrade." + upgradeType.getName()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(getFormattedModifier()).withStyle(ChatFormatting.GOLD));
    }
}
