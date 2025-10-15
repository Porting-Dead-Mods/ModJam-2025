package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJItems;
import com.portingdeadmods.modjam.registries.MJTranslations;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class MJLangProvider extends LanguageProvider {

    public MJLangProvider(PackOutput output) {
        super(output, Modjam.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.modjam", "Mod Jam");
        
        add("block.modjam.tantalum_storage_block", "Block of Tantalum");
        add("block.modjam.tantalum_ore", "Tantalum Ore");
        add("block.modjam.deepslate_tantalum_ore", "Deepslate Tantalum Ore");
        add("block.modjam.compressor", "Compressor");
        add("block.modjam.creative_power", "Creative Power Source");
        add("block.modjam.planet_simulator_controller", "Planet Simulator Controller");
        add("block.modjam.planet_simulator_bus", "Planet Simulator Bus");
        add("block.modjam.planet_simulator_part", "Planet Simulator Part");
        add("block.modjam.planet_simulator_casing", "Planet Simulator Casing");
        add("block.modjam.planet_simulator_frame", "Planet Simulator Frame");
        
        add("item.modjam.raw_tantalum", "Raw Tantalum");
        add("item.modjam.tantalum_ingot", "Tantalum Ingot");
        add("item.modjam.tantalum_nugget", "Tantalum Nugget");
        add("item.modjam.tantalum_sheet", "Tantalum Sheet");
        addItem(MJItems.PLANET_CARD, "Planet Card");
        addItem(MJItems.TINTED_PLANET_CARD, "Planet Card");
        add("item.modjam.planet_card_modjam_earth", "Earth Planet Card");
        add("item.modjam.planet_card_modjam_mars", "Mars Planet Card");
        add("item.modjam.planet_card_modjam_venus", "Venus Planet Card");
        add("item.modjam.planet_card_modjam_blackhole", "Black Hole Card");
        addItem(MJItems.ENERGY_UPGRADE, "Energy Upgrade");
        addItem(MJItems.SPEED_UPGRADE, "Speed Upgrade");
        addItem(MJItems.LUCK_UPGRADE, "Luck Upgrade");
        
        add("tooltip.modjam.upgrade.energy", "Energy Efficiency");
        add("tooltip.modjam.upgrade.speed", "Processing Speed");
        add("tooltip.modjam.upgrade.luck", "Bonus Output Chance");
        add("tooltip.modjam.no_upgrades", "No upgrades installed");
        
        add("modjam.jei.multiblock.title", "Multiblock Assembly");
        add("modjam.jei.multiblock.component", "Required Component");
        add("modjam.jei.grinding", "Grinding");
        add("jei.modjam.category.planet_simulator", "Planet Simulator");
        add("jei.modjam.category.planet_power", "Planet Power Generation");
        add("modjam.jei.toggle_exploded_view", "Show Exploded View");
        add("modjam.jei.toggle_condensed_view", "Show Condensed View");
        add("modjam.jei.all_layers_mode", "Show All Layers");
        add("modjam.jei.single_layer_mode", "Show Single Layer");
        add("modjam.jei.layer_up", "Layer Up");
        add("modjam.jei.layer_down", "Layer Down");
        
        add("container.modjam.compressor", "Compressor");
        add("jei.modjam.category.compressing", "Compressing");
        
        add("redstone_signal_type.portingdeadlibs.ignored", "Ignored");
        add("redstone_signal_type.portingdeadlibs.low_signal", "Low Signal");
        add("redstone_signal_type.portingdeadlibs.high_signal", "High Signal");

        MJTranslations.TRANSLATIONS.getDefaultTranslations().forEach(this::add);

    }
}
