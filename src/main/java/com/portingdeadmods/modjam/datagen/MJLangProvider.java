package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class MJLangProvider extends LanguageProvider {
    
    public MJLangProvider(PackOutput output) {
        super(output, Modjam.MODID, "en_us");
    }
    
    @Override
    protected void addTranslations() {
        
    }
}
