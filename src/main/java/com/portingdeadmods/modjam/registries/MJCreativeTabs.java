package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MJCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Modjam.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MODJAM_TAB = CREATIVE_TABS.register("modjam_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Modjam.MODID))
                    .icon(() -> new ItemStack(MJItems.TANTALUM_INGOT.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(MJItems.TANTALUM_STORAGE_BLOCK.get());
                        output.accept(MJItems.TANTALUM_INGOT.get());
                        output.accept(MJItems.TANTALUM_NUGGET.get());
                        output.accept(MJItems.TANTALUM_DUST.get());
                        output.accept(MJItems.TANTALUM_SHEET.get());
                        output.accept(MJItems.TANTALUM_SEMI_PRESSED_SHEET.get());
                        output.accept(MJItems.TEST_MULTIBLOCK_CONTROLLER.get());
                    })
                    .build()
    );
}
