package com.portingdeadmods.spaceploitation.content.items;

import com.portingdeadmods.spaceploitation.data.UpgradeType;
import com.portingdeadmods.spaceploitation.registries.MJRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Locale;

public class UpgradeItem extends Item {
    private final ResourceKey<UpgradeType> upgradeTypeKey;

    public UpgradeItem(ResourceKey<UpgradeType> upgradeTypeKey, Properties properties) {
        super(properties);
        this.upgradeTypeKey = upgradeTypeKey;
    }

    public ResourceKey<UpgradeType> getUpgradeTypeKey() {
        return upgradeTypeKey;
    }
    
    public UpgradeType getUpgradeType() {
        return Minecraft.getInstance().level.registryAccess()
                .lookupOrThrow(MJRegistries.UPGRADE_TYPE_KEY)
                .get(upgradeTypeKey)
                .map(holder -> holder.value())
                .orElse(new UpgradeType(List.of()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        UpgradeType upgradeType = getUpgradeType();
        for (UpgradeType.UpgradeEffect effect : upgradeType.getEffects()) {
            String effectName = formatEffectTarget(effect.getTarget());
            String effectValue = formatEffectValue(effect);
            ChatFormatting color = getEffectColor(effect);
            
            tooltip.add(Component.literal(effectName + ": " + effectValue)
                    .withStyle(color));
        }
    }
    
    private ChatFormatting getEffectColor(UpgradeType.UpgradeEffect effect) {
        boolean isBonus = switch (effect.getTarget()) {
            case DURATION, ENERGY_USAGE -> effect.getPercentPerUpgrade() < 0;
            case LUCK_CHANCE -> effect.getPercentPerUpgrade() > 0;
        };
        
        return isBonus ? ChatFormatting.GREEN : ChatFormatting.RED;
    }
    
    private String formatEffectTarget(UpgradeType.EffectTarget target) {
        return switch (target) {
            case DURATION -> "Time";
            case ENERGY_USAGE -> "Energy";
            case LUCK_CHANCE -> "Luck";
        };
    }
    
    private String formatEffectValue(UpgradeType.UpgradeEffect effect) {
        return String.format(Locale.US, "%+.1f%% per upgrade", effect.getPercentPerUpgrade());
    }
}
