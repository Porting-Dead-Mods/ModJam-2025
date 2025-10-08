package com.portingdeadmods.modjam.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.events.ClientEvents;
import com.portingdeadmods.modjam.registries.MJShaders;
import com.portingdeadmods.modjam.render.PostChainExtensions;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GL45C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4fStack;popMatrix()Lorg/joml/Matrix4fStack;"))
    void tryProcessBlackHoleShader(DeltaTracker p_348530_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_254120_, Matrix4f p_323920_, CallbackInfo ci) {
        GL45.glPushDebugGroup(GL45.GL_DEBUG_SOURCE_APPLICATION, 1, "blackhole");
        Vec3 camera = p_109604_.getPosition();
        //read ber queue?
        PostChainExtensions.setUniform(MJShaders.BLACK_HOLE_CHAIN, "SceneInvProjMat", ClientEvents.capturedProjectionMatrix.invert(new Matrix4f()));
        float near = ClientEvents.capturedProjectionMatrix.m32() / (ClientEvents.capturedProjectionMatrix.m22() - 1.0f);
        float far = ClientEvents.capturedProjectionMatrix.m32() / (ClientEvents.capturedProjectionMatrix.m22() + 1.0f);
        PostChainExtensions.setUniform(MJShaders.BLACK_HOLE_CHAIN, "InvViewMat", p_254120_.invert(new Matrix4f()));
        PostChainExtensions.setUniform(MJShaders.BLACK_HOLE_CHAIN, "ThingPosition", new float[]{(float) (camera.x - 0), (float) (camera.y - 100), (float) (camera.z - 0), (float) (camera.x - 0), (float) (camera.y - 110), (float) (camera.z - 0), 0,0,0});
        MJShaders.BLACK_HOLE_CHAIN.setUniform("Near", near);
        MJShaders.BLACK_HOLE_CHAIN.setUniform("Far", far);

        MJShaders.BLACK_HOLE_CHAIN.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight()); // bad
        RenderSystem.depthMask(false);
        MJShaders.BLACK_HOLE_CHAIN.process(Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());
        RenderSystem.depthMask(true);
        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        //flush ber queue?
        GL45.glPopDebugGroup();
    }

}
