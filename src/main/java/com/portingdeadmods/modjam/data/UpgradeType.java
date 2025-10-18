package com.portingdeadmods.modjam.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

import java.util.List;

public record UpgradeType(List<UpgradeEffect> effects) {
    public static final Codec<UpgradeType> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UpgradeEffect.CODEC.listOf().fieldOf("effects").forGetter(UpgradeType::effects)
    ).apply(inst, UpgradeType::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeType> STREAM_CODEC = StreamCodec.composite(
            UpgradeEffect.STREAM_CODEC.apply(ByteBufCodecs.list()),
            UpgradeType::effects,
            UpgradeType::new
    );

    public UpgradeEffect[] getEffects() {
        return effects.toArray(new UpgradeEffect[0]);
    }

    public record UpgradeEffect(EffectTarget target, float percentPerUpgrade) {
        public static final Codec<UpgradeEffect> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EffectTarget.CODEC.fieldOf("target").forGetter(UpgradeEffect::target),
                Codec.FLOAT.fieldOf("percent_per_upgrade").forGetter(UpgradeEffect::percentPerUpgrade)
        ).apply(inst, UpgradeEffect::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeEffect> STREAM_CODEC = StreamCodec.composite(
                EffectTarget.STREAM_CODEC,
                UpgradeEffect::target,
                ByteBufCodecs.FLOAT,
                UpgradeEffect::percentPerUpgrade,
                UpgradeEffect::new
        );

        public EffectTarget getTarget() {
            return target;
        }

        public float getPercentPerUpgrade() {
            return percentPerUpgrade;
        }
        
        public float apply(float baseValue, int upgradeCount) {
            if (upgradeCount == 0) return baseValue;
            float totalPercent = percentPerUpgrade * upgradeCount;
            return baseValue * (1.0f + totalPercent / 100.0f);
        }
    }

    public enum EffectTarget {
        DURATION,
        ENERGY_USAGE,
        LUCK_CHANCE;

        public static final Codec<EffectTarget> CODEC = Codec.STRING.xmap(
                name -> EffectTarget.valueOf(name.toUpperCase()),
                EffectTarget::name
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, EffectTarget> STREAM_CODEC = 
                StreamCodec.of(
                        (buf, target) -> buf.writeUtf(target.name()),
                        buf -> EffectTarget.valueOf(buf.readUtf())
                );
    }
}
