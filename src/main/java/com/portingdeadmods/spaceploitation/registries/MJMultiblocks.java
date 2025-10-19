package com.portingdeadmods.spaceploitation.registries;

import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.compat.jei.MultiblockJEIPlugin;
import com.portingdeadmods.spaceploitation.content.multiblock.PlanetSimulatorMultiblock;
import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MJMultiblocks {
    public static final DeferredRegister<Multiblock> MULTIBLOCKS = DeferredRegister.create(PDLRegistries.MULTIBLOCK, Spaceploitation.MODID);

    public static final Supplier<PlanetSimulatorMultiblock> PLANET_SIMULATOR = MULTIBLOCKS.register("planet_simulator", PlanetSimulatorMultiblock::new);
    
    public static void init() {
        MultiblockJEIPlugin.registerMultiblock(PLANET_SIMULATOR);
    }

}
