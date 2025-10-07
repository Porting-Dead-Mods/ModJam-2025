package com.portingdeadmods.modjam.mixin;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJShaders;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GL45C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V", ordinal = 0))
    void tryProcessBlackHoleShader(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci)
    {
        GL45.glPushDebugGroup(GL45.GL_DEBUG_SOURCE_APPLICATION, 1, "blackhole");
        //read ber queue?
        MJShaders.BLACK_HOLE_CHAIN.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight()); // bad
        MJShaders.BLACK_HOLE_CHAIN.process(Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());
        //flush ber queue?
        GL45.glPopDebugGroup();
    }

}
