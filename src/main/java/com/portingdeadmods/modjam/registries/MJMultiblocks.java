package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.compat.jei.MultiblockJEIPlugin;
import com.portingdeadmods.modjam.content.multiblock.PlanetSimulatorMultiblock;
import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MJMultiblocks {
    public static final DeferredRegister<Multiblock> MULTIBLOCKS = DeferredRegister.create(PDLRegistries.MULTIBLOCK, Modjam.MODID);

    public static final Supplier<PlanetSimulatorMultiblock> PLANET_SIMULATOR = MULTIBLOCKS.register("planet_simulator", PlanetSimulatorMultiblock::new);
    
    public static void init() {
        MultiblockJEIPlugin.registerMultiblock(PLANET_SIMULATOR);
    }

}
