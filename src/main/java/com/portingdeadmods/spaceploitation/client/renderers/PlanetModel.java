package com.portingdeadmods.spaceploitation.client.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector4f;

//redundent
public class PlanetModel {

    public static void renderPlanetModelToBuffer(Matrix4f view, VertexConsumer consumer, Vector4f color)
    {
        consumer.addVertex(view, -0.5f, -0.5f, 0.5f).setUv(0.25f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, -1f, 1f);
        consumer.addVertex(view, 0.5f, -0.5f, 0.5f).setUv(0.5f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, -1f, 1f);
        consumer.addVertex(view, 0.5f, 0.5f, 0.5f).setUv(0.5f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, 1f, 1f);
        consumer.addVertex(view, -0.5f, 0.5f, 0.5f).setUv(0.25f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, 1f, 1f);

        consumer.addVertex(view, 0.5f, -0.5f, 0.5f).setUv(0.5f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, -1f, 1f);
        consumer.addVertex(view, 0.5f, -0.5f, -0.5f).setUv(0.75f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, -1f, 1f);
        consumer.addVertex(view, 0.5f, 0.5f, -0.5f).setUv(0.75f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, 1f, 1f);
        consumer.addVertex(view, 0.5f, 0.5f, 0.5f).setUv(0.5f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, 1f, 1f);

        consumer.addVertex(view, 0.5f, -0.5f, -0.5f).setUv(0.75f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, -1f, 1f);
        consumer.addVertex(view, -0.5f, -0.5f, -0.5f).setUv(1, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, -1f, 1f);
        consumer.addVertex(view, -0.5f, 0.5f, -0.5f).setUv(1, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, 1f, 1f);
        consumer.addVertex(view, 0.5f, 0.5f, -0.5f).setUv(0.75f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, 1f, 1f);

        consumer.addVertex(view, -0.5f, -0.5f, -0.5f).setUv(0.25f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, -1f, 1f);
        consumer.addVertex(view, -0.5f, -0.5f, 0.5f).setUv(0f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, -1f, 1f);
        consumer.addVertex(view, -0.5f, 0.5f, 0.5f).setUv(0f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, 1f, 1f);
        consumer.addVertex(view, -0.5f, 0.5f, -0.5f).setUv(0.25f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, 1f, 1f);

        consumer.addVertex(view, 0.5f, 0.5f, -0.5f).setUv(0.25f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, 1f, -1f);
        consumer.addVertex(view, -0.5f, 0.5f, -0.5f).setUv(0.5f, 0.25f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, 1f, -1f);
        consumer.addVertex(view, -0.5f, 0.5f, 0.5f).setUv(0.5f, 0f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, 1f, 1f);
        consumer.addVertex(view, 0.5f, 0.5f, 0.5f).setUv(0.25f, 0f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, 1f, 1f);

        consumer.addVertex(view, -0.5f, -0.5f, 0.5f).setUv(0.25f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, -1f, 4f);
        consumer.addVertex(view, -0.5f, -0.5f, -0.5f).setUv(0.25f, 0.75f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, -1f, -4f);
        consumer.addVertex(view, 0.5f, -0.5f, -0.5f).setUv(0.5f, 0.75f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, -1f, -4f);
        consumer.addVertex(view, 0.5f, -0.5f, 0.5f).setUv(0.5f, 0.5f).setColor(color.x, color.y, color.z, color.w).setNormal(1f, -1f, 4f);

//        consumer.addVertex(view, 0.5f, -0.5f, 0.5f).setUv(0.5f, 0.75f).setColor(color.x, color.y, color.z, color.w).setNormal(-1f, -1f, 1f); // Bottom-left

    }

    public static void renderPlanetToModelDepth(Matrix4f view, VertexConsumer consumer, Vector4f color)
    {
        consumer.addVertex(view, -0.5f, -0.5f, 0.5f);
        consumer.addVertex(view, 0.5f, -0.5f, 0.5f);
        consumer.addVertex(view, 0.5f, 0.5f, 0.5f);
        consumer.addVertex(view, -0.5f, 0.5f, 0.5f);

        consumer.addVertex(view, 0.5f, -0.5f, 0.5f);
        consumer.addVertex(view, 0.5f, -0.5f, -0.5f);
        consumer.addVertex(view, 0.5f, 0.5f, -0.5f);
        consumer.addVertex(view, 0.5f, 0.5f, 0.5f);

        consumer.addVertex(view, 0.5f, -0.5f, -0.5f);
        consumer.addVertex(view, -0.5f, -0.5f, -0.5f);
        consumer.addVertex(view, -0.5f, 0.5f, -0.5f);
        consumer.addVertex(view, 0.5f, 0.5f, -0.5f);

        consumer.addVertex(view, -0.5f, -0.5f, -0.5f);
        consumer.addVertex(view, -0.5f, -0.5f, 0.5f);
        consumer.addVertex(view, -0.5f, 0.5f, 0.5f);
        consumer.addVertex(view, -0.5f, 0.5f, -0.5f);

        consumer.addVertex(view, 0.5f, 0.5f, -0.5f);
        consumer.addVertex(view, -0.5f, 0.5f, -0.5f);
        consumer.addVertex(view, -0.5f, 0.5f, 0.5f);
        consumer.addVertex(view, 0.5f, 0.5f, 0.5f);

        consumer.addVertex(view, -0.5f, -0.5f, -0.5f);
        consumer.addVertex(view, 0.5f, -0.5f, -0.5f);
        consumer.addVertex(view, 0.5f, -0.5f, 0.5f);
        consumer.addVertex(view, -0.5f, -0.5f, 0.5f);
    }

}
