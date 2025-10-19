package com.portingdeadmods.spaceploitation.registries;

import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.portingdeadmods.spaceploitation.Spaceploitation;
import com.portingdeadmods.spaceploitation.render.PostChainExtensions;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.lwjgl.opengl.GL45;

import java.io.IOException;
import java.util.function.Function;

public class MJShaders {

   // public static final VertexFormat POSITION_TEX_COLOR_NORMAL = VertexFormat.builder().add("Position", VertexFormatElement.POSITION).add("UV0",VertexFormatElement.UV0).add("Color", VertexFormatElement.COLOR).add("Normal",VertexFormatElement.NORMAL).build();

    public static ShaderInstance PROJECTOR_SHADER;

    public static ShaderInstance PLANET_PROJECTION_SHADER;

    public static ShaderInstance BLACK_HOLE_SHADER;

    public static PostChain BLACK_HOLE_CHAIN;

    public static int BLACK_HOLE_UBO_ID;

//    public stati

    public static final RenderStateShard.ShaderStateShard PROJECTOR_SHADER_SHARD = new RenderStateShard.ShaderStateShard(
            () -> {
                return PROJECTOR_SHADER;
            });

    public static final RenderStateShard.ShaderStateShard PLANET_PROJECTION_SHADER_SHARD = new RenderStateShard.ShaderStateShard(
            () -> {
                return PLANET_PROJECTION_SHADER;
            });

    public static final RenderType PROJECTOR_SHARD = RenderType.create("spaceploitation:projector_rendertype", DefaultVertexFormat.POSITION_TEX_COLOR,
            VertexFormat.Mode.QUADS, 80, false, false, RenderType.CompositeState.builder()
                    .setShaderState(PROJECTOR_SHADER_SHARD)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(false));

    public static final Function<ResourceLocation, RenderType> PLANET_PROJECTION = Util.memoize( (ResourceLocation textureLocation) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setShaderState(PLANET_PROJECTION_SHADER_SHARD)
                .setTextureState(new RenderStateShard.TextureStateShard(textureLocation, false, true))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderStateShard.NO_CULL)
                .createCompositeState(false);

        return RenderType.create("spaceploitation:planet_projection_rendertype", DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, VertexFormat.Mode.QUADS, 320, false, true, compositeState);
    });

public static final RenderType PLANET_PROJECTION_DEPTH =
        RenderType.create("spaceploitation:planet_projection_depth_rendertype", DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, VertexFormat.Mode.QUADS, 320, false, false, RenderType.CompositeState.builder()
                .setShaderState(PLANET_PROJECTION_SHADER_SHARD)
                .setWriteMaskState(RenderStateShard.DEPTH_WRITE)
                .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderStateShard.NO_CULL)
                .createCompositeState(false));

    public static class ProjecterShaderUniforms {

    }

    public static class PlanetProjectionShaderUniforms {
        public static AbstractUniform GRID_SIZE;
        public static AbstractUniform GRID_WIDTH;
        public static AbstractUniform NOISE_SCALE;
        public static AbstractUniform NOISE_THRESHOLD;
        public static AbstractUniform NOISE_PIXELIZATION;
        public static AbstractUniform NOISE_SPEED;
        public static AbstractUniform NOISE_DIRECTION;
        public static AbstractUniform TINT;
        public static AbstractUniform GRID_COLOR;
        public static AbstractUniform FLICKER_RATE;
        public static AbstractUniform FLICKER_INTENSITY;
        public static AbstractUniform PROGRESS;


    }

    public static void registerShaders(RegisterShadersEvent shadersEvent) {
//        shadersEvent.registerShader(compileShader(shadersEvent, ResourceLocation.fromNamespaceAndPath(Spaceploitation.MODID, "projector"), DefaultVertexFormat.POSITION_TEX_COLOR), (shaderInstance -> {
//            if (shaderInstance != null) {
//                PROJECTOR_SHADER = shaderInstance;
//            }
//        }));
//
        shadersEvent.registerShader(compileShader(shadersEvent, ResourceLocation.fromNamespaceAndPath(Spaceploitation.MODID, "planet_projection"), DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL), (shaderInstance -> {
            if (shaderInstance != null) {
                PLANET_PROJECTION_SHADER = shaderInstance;
                PlanetProjectionShaderUniforms.GRID_SIZE = shaderInstance.safeGetUniform("GridSize");
                PlanetProjectionShaderUniforms.GRID_WIDTH = shaderInstance.safeGetUniform("GridWidth");
                PlanetProjectionShaderUniforms.NOISE_SCALE = shaderInstance.safeGetUniform("NoiseScale");
                PlanetProjectionShaderUniforms.NOISE_THRESHOLD = shaderInstance.safeGetUniform("NoiseThreshold");
                PlanetProjectionShaderUniforms.NOISE_PIXELIZATION = shaderInstance.safeGetUniform("NoisePixelization");
                PlanetProjectionShaderUniforms.NOISE_SPEED = shaderInstance.safeGetUniform("NoiseSpeed");
                PlanetProjectionShaderUniforms.NOISE_DIRECTION = shaderInstance.safeGetUniform("NoiseDirection");
                PlanetProjectionShaderUniforms.TINT = shaderInstance.safeGetUniform("Tint");
                PlanetProjectionShaderUniforms.GRID_COLOR = shaderInstance.safeGetUniform("GridColor");
                PlanetProjectionShaderUniforms.FLICKER_RATE = shaderInstance.safeGetUniform("FlickerRate");
                PlanetProjectionShaderUniforms.FLICKER_INTENSITY = shaderInstance.safeGetUniform("FlickerIntensity");
                PlanetProjectionShaderUniforms.PROGRESS = shaderInstance.safeGetUniform("Progress");

            }
        }));

//        shadersEvent.registerShader(compileShader(shadersEvent, ResourceLocation.fromNamespaceAndPath(Spaceploitation.MODID, "blackhole"), DefaultVertexFormat.POSITION), (shaderInstance -> {
//            if (shaderInstance != null)
//            {
//                BLACK_HOLE_SHADER = shaderInstance;
//            }
//        }));

        GL45.glDeleteBuffers(BLACK_HOLE_UBO_ID);
        int bufferId = GL45.glGenBuffers();
        GL45.glBindBuffer(GL45.GL_UNIFORM_BUFFER, bufferId);
        GL45.glBufferData(GL45.GL_UNIFORM_BUFFER, 100, GL45.GL_DYNAMIC_DRAW);
        BLACK_HOLE_UBO_ID = bufferId;

        try {
            BLACK_HOLE_CHAIN = new PostChain(Minecraft.getInstance().getTextureManager(), shadersEvent.getResourceProvider(), Minecraft.getInstance().getMainRenderTarget(), ResourceLocation.fromNamespaceAndPath(Spaceploitation.MODID, "shaders/post/blackhole.json"));

            int shaderId = ((PostChainExtensions)BLACK_HOLE_CHAIN).getPasses().get(0).getEffect().getId();
            GL45.glUseProgram(shaderId);
            int uniformLocation = GL45.glGetUniformBlockIndex(shaderId, "BlackHoleList");
            GL45.glUniformBlockBinding(shaderId, uniformLocation, 1);

        } catch (Exception e) {
            e.printStackTrace();
            Spaceploitation.LOGGER.error(e.getMessage());
        }
    }

    private static ShaderInstance compileShader(RegisterShadersEvent event, ResourceLocation location, VertexFormat vertexFormat) {
        try {
            return new ShaderInstance(event.getResourceProvider(), location, vertexFormat);
        } catch (ChainedJsonException jsonException) {
            Spaceploitation.LOGGER.error(jsonException.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
