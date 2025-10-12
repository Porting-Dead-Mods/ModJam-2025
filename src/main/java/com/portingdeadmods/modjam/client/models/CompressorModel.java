package com.portingdeadmods.modjam.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.portingdeadmods.modjam.Modjam;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;

public class CompressorModel extends Model {
    public static final Material MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, Modjam.rl("entity/compressor"));
    public static final Material PRESS_MATERIAL = new Material(InventoryMenu.BLOCK_ATLAS, Modjam.rl("entity/compressor_press"));
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Modjam.rl("compressor"), "main");
    public static final ModelLayerLocation PRESS_LAYER_LOCATION = new ModelLayerLocation(Modjam.rl("compressor_press"), "main");
    private final ModelPart main;

    public CompressorModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -2.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-8.0F, -16.0F, -2.0F, 3.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(14, 48).addBox(5.0F, -16.0F, -2.0F, 3.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-7.0F, -12.0F, -7.0F, 4.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(3.0F, -12.0F, -7.0F, 4.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).addBox(-3.0F, -12.0F, 3.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(56, 6).addBox(-3.0F, -12.0F, -7.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 49).addBox(-8.0F, -26.0F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(42, 49).addBox(5.0F, -26.0F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(36, 40).addBox(-5.0F, -23.0F, -3.0F, 10.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(36, 16).addBox(-6.0F, -10.0F, 2.0F, 12.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(36, 28).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public static LayerDefinition createPressBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}