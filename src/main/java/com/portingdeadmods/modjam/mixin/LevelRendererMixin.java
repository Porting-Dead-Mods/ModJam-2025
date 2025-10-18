package com.portingdeadmods.modjam.mixin;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJShaders;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL45;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4fStack;popMatrix()Lorg/joml/Matrix4fStack;"))
    void tryProcessBlackHoleShader(DeltaTracker p_348530_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_254120_, Matrix4f p_323920_, CallbackInfo ci) {
        GL45.glPushDebugGroup(GL45.GL_DEBUG_SOURCE_APPLICATION, 1, "blackhole");
        //read ber queue?
//        PostChainExtensions.setUniform(MJShaders.BLACK_HOLE_CHAIN, "SceneInvProjMat", ClientEvents.capturedProjectionMatrix.invert(new Matrix4f()));
//        float near = ClientEvents.capturedProjectionMatrix.m32() / (ClientEvents.capturedProjectionMatrix.m22() - 1.0f);
//        float far = ClientEvents.capturedProjectionMatrix.m32() / (ClientEvents.capturedProjectionMatrix.m22() + 1.0f);
//        PostChainExtensions.setUniform(MJShaders.BLACK_HOLE_CHAIN, "InvViewMat", p_254120_.invert(new Matrix4f()));
//        MJShaders.BLACK_HOLE_CHAIN.setUniform("Near", near);
//        MJShaders.BLACK_HOLE_CHAIN.setUniform("Far", far);
//
//        GL45.glBindBuffer(GL45.GL_UNIFORM_BUFFER, MJShaders.BLACK_HOLE_UBO_ID);
//        GL45.glBufferSubData(GL45.GL_UNIFORM_BUFFER, 0, BlackHoleExampleRenderer.blackholeUniformBuffer.build());
//
//        GL45.glBindBufferBase(GL45.GL_UNIFORM_BUFFER, 1, MJShaders.BLACK_HOLE_UBO_ID);
//
//        GL45.glBindBuffer(GL45.GL_UNIFORM_BUFFER, 0);
//
//
//        MJShaders.BLACK_HOLE_CHAIN.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight()); // bad
//        RenderSystem.depthMask(false);
//        MJShaders.BLACK_HOLE_CHAIN.process(Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());
//
//        GL45.glBindBufferBase(GL45.GL_UNIFORM_BUFFER, 1, 0);
//
//
//        RenderSystem.depthMask(true);
//        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        //flush ber queue?

        MultiBufferSource.BufferSource buf = MultiBufferSource.immediate(new ByteBufferBuilder(12));
        VertexConsumer consumer = buf.getBuffer(MJShaders.PLANET_PROJECTION.apply(Modjam.rl("textures/planet/ass.png")));


        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        Matrix4f view = new Matrix4f().translate((float) -cam.x, (float) -cam.y, (float) -cam.z);
//        PlanetSimulatorBlockEntityRenderer.prepareUniforms(8, 0.125f, 8, 16, 0, new Vector3f(0,0,1), new Vector3f(0.2f, 0.3f, 0.833f));
       // PlanetModel.renderPlanetModelToBuffer(view, consumer, new Vector4f(1,1,1,1));

//        consumer.addVertex(view, 0,0,0).setUv(0,0).setColor(0xffffffff).setNormal(-1,-1,-1);
//        consumer.addVertex(view, 0,1,0).setUv(0,1).setColor(0xffffffff).setNormal(-1,1,-1);
//        consumer.addVertex(view, 1,1,0).setUv(1,1).setColor(0xffffffff).setNormal(1,1,-1);
//        consumer.addVertex(view, 1,0,0).setUv(1,0).setColor(0xffffffff).setNormal(1,-1,-1);
        //buf.endBatch(MJShaders.PLANET_PROJECTION.apply(Modjam.rl("textures/planet/ass.png")));

        GL45.glPopDebugGroup();
        //BlackHoleExampleRenderer.blackholeUniformBuffer.restart();
    }

}
