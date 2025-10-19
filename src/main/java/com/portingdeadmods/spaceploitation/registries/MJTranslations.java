package com.portingdeadmods.spaceploitation.registries;

import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslation;
import com.portingdeadmods.portingdeadlibs.api.translations.DeferredTranslationRegister;
import com.portingdeadmods.portingdeadlibs.api.translations.TranslatableConstant;
import com.portingdeadmods.portingdeadlibs.api.translations.TranslationCategory;

public final class MJTranslations {
    public static final DeferredTranslationRegister TRANSLATIONS = DeferredTranslationRegister.createTranslations(Spaceploitation.MODID);

    public static final TranslationCategory TOOLTIP_CATEGORY = TRANSLATIONS.createCategory("tooltip");

    public static final DeferredTranslation<TranslatableConstant> PLANET_CARD = TOOLTIP_CATEGORY.registerWithDefault("planet_card", "Dimension: %s");

}
