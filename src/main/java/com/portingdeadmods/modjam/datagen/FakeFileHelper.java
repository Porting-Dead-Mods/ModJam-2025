package com.portingdeadmods.modjam.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class FakeFileHelper extends ExistingFileHelper {
    public FakeFileHelper() {
        super(java.util.Collections.emptyList(), java.util.Collections.emptySet(), false, null, null);
    }

    @Override
    public boolean exists(ResourceLocation loc, PackType packType) {
        return true;
    }

    @Override
    public boolean exists(ResourceLocation loc, IResourceType type) {
        return true;
    }

    @Override
    public boolean exists(ResourceLocation loc, PackType packType, String pathSuffix, String pathPrefix) {
        return true;
    }
}