package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.modjam.registries.MJItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MJItemModelProvider extends ItemModelProvider {

    public MJItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Modjam.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MJItems.TANTALUM_INGOT.get());
        basicItem(MJItems.TANTALUM_NUGGET.get());
        basicItem(MJItems.TANTALUM_SHEET.get());
        basicItem(MJItems.RAW_TANTALUM.get());
        simpleBlockItem(MJBlocks.PLANET_SIMULATOR_CONTROLLER.get());
        simpleBlockItem(MJBlocks.PLANET_SIMULATOR_BUS.get());
        //basicItem(MJItems.PESTLE.get());
        //basicItem(MJItems.MORTAR.get());
        //basicItem(MJItems.HAMMER.get());
    }

}
