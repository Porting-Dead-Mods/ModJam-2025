package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.content.menus.CompressorMenu;
import com.portingdeadmods.modjam.content.menus.EnergyBusMenu;
import com.portingdeadmods.modjam.content.menus.FluidBusMenu;
import com.portingdeadmods.modjam.content.menus.ItemBusMenu;
import com.portingdeadmods.modjam.content.menus.PlanetSimulatorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MJMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Modjam.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CompressorMenu>> COMPRESSOR =
            MENUS.register("compressor", () -> IMenuTypeExtension.create(CompressorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<PlanetSimulatorMenu>> PLANET_SIMULATOR =
            MENUS.register("planet_simulator", () -> IMenuTypeExtension.create(PlanetSimulatorMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ItemBusMenu>> ITEM_BUS =
            MENUS.register("item_bus", () -> IMenuTypeExtension.create(ItemBusMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<EnergyBusMenu>> ENERGY_BUS =
            MENUS.register("energy_bus", () -> IMenuTypeExtension.create(EnergyBusMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<FluidBusMenu>> FLUID_BUS =
            MENUS.register("fluid_bus", () -> IMenuTypeExtension.create(FluidBusMenu::new));

}
