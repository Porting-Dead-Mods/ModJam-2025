package com.portingdeadmods.modjam.datagen;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.client.models.item.PlanetCardItemModel;
import com.portingdeadmods.modjam.content.items.PlanetCardItem;
import com.portingdeadmods.modjam.registries.MJBlocks;
import com.portingdeadmods.modjam.registries.MJItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        planetCard(MJItems.PLANET_CARD.get());
        tintedPlanetCard(MJItems.TINTED_PLANET_CARD.get());

        basicItem(MJItems.ENERGY_UPGRADE.get());
        basicItem(MJItems.SPEED_UPGRADE.get());
        basicItem(MJItems.LUCK_UPGRADE.get());
        basicItem(MJItems.GUIDE.get());

        Set<Block> noItemModels = MJBlocks.NO_ITEM_MODELS.stream().map(DeferredHolder::get).collect(Collectors.toSet());

        MJBlocks.BLOCKS.getBlockItems().stream()
                .map(Supplier::get)
                .map(Block::byItem)
                .filter(b -> !noItemModels.contains(b))
                .forEach(this::simpleBlockItem);
    }

    private ItemModelBuilder planetCard(PlanetCardItem item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        return getBuilder(name)
                .customLoader(PlanetCardItemModel.LoaderBuilder::new).end()
                .parent(new ModelFile.UncheckedModelFile("neoforge:item/default"));
    }

    private ItemModelBuilder tintedPlanetCard(PlanetCardItem item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        return getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", Modjam.rl("item/planet_card/base"))
                .texture("layer1", Modjam.rl("item/planet_card/planet_tint"))
                .element()
                    .from(0, 0, 0)
                    .to(16, 16, 0)
                    .face(Direction.SOUTH)
                        .uvs(0, 0, 16, 16)
                        .texture("#layer0")
                        .end()
                    .end()
                .element()
                    .from(0, 0, 0)
                    .to(16, 16, 0)
                    .face(Direction.SOUTH)
                        .uvs(0, 0, 16, 16)
                        .texture("#layer1")
                        .tintindex(1)
                        .end()
                    .end();
    }
}
