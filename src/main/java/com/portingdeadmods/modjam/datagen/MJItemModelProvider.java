package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MJItemModelProvider extends ItemModelProvider {
    
    public MJItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Modjam.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        
    }
}
