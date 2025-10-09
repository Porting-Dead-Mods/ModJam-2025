package com.portingdeadmods.modjam;

import com.mojang.logging.LogUtils;
import com.portingdeadmods.modjam.networking.MJNetworking;
import com.portingdeadmods.modjam.registries.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(Modjam.MODID)
public class Modjam {
    public static final String MODID = "modjam";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Modjam(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        MJItems.ITEMS.register(modEventBus);
        MJBlocks.BLOCKS.register(modEventBus);
        MJBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        MJMenus.MENUS.register(modEventBus);
        MJCreativeTabs.CREATIVE_TABS.register(modEventBus);
        MJDataComponents.DATA_COMPONENTS.register(modEventBus);
        MJDataAttachments.ATTACHMENTS.register(modEventBus);
        MJRecipes.RECIPE_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(MJNetworking::register);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(MJMultiblocks::init);
    }

}
