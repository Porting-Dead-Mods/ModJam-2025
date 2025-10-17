package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MJCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Modjam.MODID);

    static {
        CREATIVE_TABS.register("modjam_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + Modjam.MODID))
                        .icon(() -> new ItemStack(MJItems.TANTALUM_INGOT.get()))
                        .displayItems((parameters, output) -> {
                            output.accept(MJItems.GUIDE.get().getDefaultInstance());
                            
                            output.acceptAll(MJItems.ITEMS.getCreativeTabItems().stream()
                                    .map(Supplier::get)
                                    .filter(item -> item != MJItems.TINTED_PLANET_CARD.get())
                                    .map(Item::getDefaultInstance)
                                    .toList());
                            
                            MJPlanetCards.getPlanetCardStacks().forEach(output::accept);
                        })
                        .build()
        );
    }
}
