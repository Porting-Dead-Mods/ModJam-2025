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
        add("itemGroup.modjam", "Mod Jam");
        
        add("block.modjam.test_multiblock_controller", "Test Multiblock Controller");
        add("block.modjam.test_multiblock_controller_formed", "Test Multiblock Controller (Formed)");
        add("block.modjam.tantalum_storage_block", "Block of Tantalum");
        
        add("item.modjam.tantalum_ingot", "Tantalum Ingot");
        add("item.modjam.tantalum_nugget", "Tantalum Nugget");
        add("item.modjam.tantalum_dust", "Tantalum Dust");
        add("item.modjam.tantalum_sheet", "Tantalum Sheet");
        add("item.modjam.tantalum_semi_pressed_sheet", "Semi-Pressed Tantalum Sheet");
        
        add("modjam.jei.multiblock.title", "Multiblock Assembly");
        add("modjam.jei.multiblock.component", "Required Component");
        add("modjam.jei.toggle_exploded_view", "Show Exploded View");
        add("modjam.jei.toggle_condensed_view", "Show Condensed View");
        add("modjam.jei.all_layers_mode", "Show All Layers");
        add("modjam.jei.single_layer_mode", "Show Single Layer");
        add("modjam.jei.layer_up", "Layer Up");
        add("modjam.jei.layer_down", "Layer Down");
    }
}
