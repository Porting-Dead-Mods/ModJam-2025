package com.portingdeadmods.spaceploitation.client.models.item;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;
import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.data.PlanetComponent;
import com.portingdeadmods.spaceploitation.data.PlanetType;
import com.portingdeadmods.spaceploitation.registries.MJDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.CompositeModel;
import net.neoforged.neoforge.client.model.IModelBuilder;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.geometry.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public record PlanetCardItemModel(Optional<ResourceKey<Level>> planet, Optional<PlanetType> planetType) implements IUnbakedGeometry<PlanetCardItemModel> {

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
        ResourceLocation overlayLoc = planetType.map(this::resolveTexture)
                .orElseGet(() -> resolveTextureOrTint(planet.orElse(ResourceKey.create(Registries.DIMENSION, Spaceploitation.rl("empty")))));
        
        TextureAtlasSprite overlaySprite = spriteGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, overlayLoc));
        TextureAtlasSprite baseSprite = spriteGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, Spaceploitation.rl("item/planet_card/base")));
        var itemContext = StandaloneGeometryBakingContext.builder(context).withGui3d(false).withUseBlockLight(false).build(Spaceploitation.rl("planet_card_override"));
        var builder = CompositeModel.Baked.builder(itemContext, baseSprite, new CardOverrideHandler(overrides, baker, itemContext), context.getTransforms());
        var renderTypes = new RenderTypeGroup(RenderType.translucent(), NeoForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get());
        var transformedState = new SimpleModelState(modelState.getRotation().compose(new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1.002f), new Quaternionf())), modelState.isUvLocked());
        
        builder.addLayer(bakeLayer(baseSprite, transformedState, itemContext, context.getTransforms(), renderTypes, overrides, baker));
        builder.addLayer(bakeLayer(overlaySprite, transformedState, itemContext, context.getTransforms(), renderTypes, overrides, baker));
        builder.setParticle(baseSprite);
        return builder.build();
    }
    
    private BakedModel bakeLayer(TextureAtlasSprite sprite, ModelState transformedState, IGeometryBakingContext itemContext, ItemTransforms transforms, RenderTypeGroup renderTypes, ItemOverrides overrides, ModelBaker baker) {
        var unbaked = UnbakedGeometryHelper.createUnbakedItemElements(0, sprite);
        var quads = UnbakedGeometryHelper.bakeElements(unbaked, material -> sprite, transformedState);
        var model = IModelBuilder.of(itemContext.useAmbientOcclusion(), itemContext.useBlockLight(), itemContext.isGui3d(), transforms, new CardOverrideHandler(overrides, baker, itemContext), sprite, renderTypes);
        quads.forEach(model::addUnculledFace);
        return model.build();
    }

    private ResourceLocation resolveTextureOrTint(ResourceKey<Level> planetKey) {
        String planetName = planet.map(levelResourceKey -> levelResourceKey.location().getPath()).orElse("empty");
        String planetOwner = planet.map(levelResourceKey -> levelResourceKey.location().getNamespace()).orElse(Spaceploitation.MODID);
        return ResourceLocation.fromNamespaceAndPath(planetOwner, "item/planet_card/planets/" + planetName);
    }
    
    private ResourceLocation resolveTexture(PlanetType planetType) {
        return planetType.texture();
    }

    public static class Loader implements IGeometryLoader<PlanetCardItemModel>
    {
        @Override
        public PlanetCardItemModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException
        {
            return new PlanetCardItemModel(Optional.empty(), Optional.empty());
        }
    }

    public static class LoaderBuilder extends CustomLoaderBuilder<ItemModelBuilder> {
        public LoaderBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(Spaceploitation.rl("planet_card"), parent, existingFileHelper, false);
        }
    }

    private static class CardOverrideHandler extends ItemOverrides {
        private final Map<String, BakedModel> cache = Maps.newHashMap(); // contains all the baked models since they'll never change
        private final ItemOverrides nested;
        private final ModelBaker baker;
        private final IGeometryBakingContext owner;

        private CardOverrideHandler(ItemOverrides nested, ModelBaker baker, IGeometryBakingContext owner) {
            this.nested = nested;
            this.baker = baker;
            this.owner = owner;
        }

        @Override
        @Nullable
        @ParametersAreNonnullByDefault
        public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            BakedModel overridden = nested.resolve(originalModel, stack, level, entity, seed);
            if (overridden != originalModel || level == null) return overridden;
            final PlanetComponent planetComponent = stack.get(MJDataComponents.PLANET);
            final Optional<PlanetType> planetType = planetComponent != null ? planetComponent.planetType() : Optional.empty();
            final String name = planetType.map(pt -> "type:" + pt.texture().toString()).orElse(Spaceploitation.MODID + ":empty");
            if (!cache.containsKey(name)) {
                PlanetCardItemModel unbaked = new PlanetCardItemModel(Optional.empty(), planetType);
                BakedModel bakedModel = unbaked.bake(owner, baker, Material::sprite, BlockModelRotation.X0_Y0, this);
                cache.put(name, bakedModel);
                return bakedModel;
            }
            return cache.get(name);
        }
    }
}
