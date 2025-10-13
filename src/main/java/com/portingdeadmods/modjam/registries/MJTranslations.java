package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslation;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslationRegister;
import com.portingdeadmods.portingdeadlibs.api.translations.TranslatableConstant;
import com.portingdeadmods.portingdeadlibs.api.translations.TranslationCategory;
import net.neoforged.fml.loading.moddiscovery.ModJarMetadata;
import net.neoforged.neoforge.registries.DeferredItem;

public final class MJTranslations {
    public static final DeferredTranslationRegister TRANSLATIONS = DeferredTranslationRegister.createTranslations(Modjam.MODID);

    public static final TranslationCategory TOOLTIP_CATEGORY = TRANSLATIONS.createCategory("tooltip");

    public static final DeferredTranslation<TranslatableConstant> PLANET_CARD = TOOLTIP_CATEGORY.registerWithDefault("planet_card", "Dimension: %s");

}
