package com.portingdeadmods.modjam.registries;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.portingdeadmods.modjam.Modjam;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Function;

public class MJShaders {
    public static ShaderInstance PROJECTOR_SHADER;

    public static ShaderInstance BLACK_HOLE_SHADER;

    public static PostChain BLACK_HOLE_CHAIN;

    public static final RenderStateShard.ShaderStateShard PROJECTOR_SHADER_SHARD = new RenderStateShard.ShaderStateShard(
            () -> {
                return PROJECTOR_SHADER;
            });

//    public static final RenderStateShard.ShaderStateShard BLACK_HOLE_SHADER_SHARD = new RenderStateShard.ShaderStateShard(
//            () -> {
//                return BLACK_HOLE_SHADER;
//            });

    public static final RenderType PROJECTOR_SHARD = RenderType.create("modjam:projector_rendertype", DefaultVertexFormat.POSITION_TEX_COLOR,
            VertexFormat.Mode.QUADS, 80, false, false, RenderType.CompositeState.builder()
                    .setShaderState(PROJECTOR_SHADER_SHARD)
                    .createCompositeState(false));

    public static class ProjecterShaderUniforms {

    }

    public static void registerShaders(RegisterShadersEvent shadersEvent) {
        shadersEvent.registerShader(compileShader(shadersEvent, ResourceLocation.fromNamespaceAndPath(Modjam.MODID, "projector"), DefaultVertexFormat.POSITION_TEX_COLOR), (shaderInstance -> {
            if (shaderInstance != null) {
                PROJECTOR_SHADER = shaderInstance;
            }
        }));

//        shadersEvent.registerShader(compileShader(shadersEvent, ResourceLocation.fromNamespaceAndPath(Modjam.MODID, "blackhole"), DefaultVertexFormat.POSITION), (shaderInstance -> {
//            if (shaderInstance != null)
//            {
//                BLACK_HOLE_SHADER = shaderInstance;
//            }
//        }));

        try {
            BLACK_HOLE_CHAIN = new PostChain(Minecraft.getInstance().getTextureManager(), shadersEvent.getResourceProvider(), Minecraft.getInstance().getMainRenderTarget(), ResourceLocation.fromNamespaceAndPath(Modjam.MODID, "shaders/post/blackhole.json"));
        } catch (Exception e) {
            e.printStackTrace();
            Modjam.LOGGER.error(e.getMessage());
        }
    }

    private static ShaderInstance compileShader(RegisterShadersEvent event, ResourceLocation location, VertexFormat vertexFormat) {
        try {
            return new ShaderInstance(event.getResourceProvider(), location, vertexFormat);
        } catch (ChainedJsonException jsonException) {
            Modjam.LOGGER.error(jsonException.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
