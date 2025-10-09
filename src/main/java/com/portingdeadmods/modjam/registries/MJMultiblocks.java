package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.compat.jei.MultiblockJEIPlugin;
import com.portingdeadmods.modjam.multiblock.TestMultiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;

public class MJMultiblocks {
    public static final Multiblock TEST_MULTIBLOCK = new TestMultiblock();
    
    public static void init() {
        MultiblockJEIPlugin.registerMultiblock(TEST_MULTIBLOCK);
    }
}
