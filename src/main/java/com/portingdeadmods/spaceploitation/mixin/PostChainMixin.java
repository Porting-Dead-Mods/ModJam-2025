package com.portingdeadmods.spaceploitation.mixin;

import com.portingdeadmods.spaceploitation.render.PostChainExtensions;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
// use accses

@Mixin(PostChain.class)
public class PostChainMixin implements PostChainExtensions {

    @Final
    @Shadow
    private List<PostPass> passes;

    @Override
    public void _setUniform(String uniformName, Matrix4f matrix) {
        for (PostPass postpass : this.passes) {
            postpass.getEffect().safeGetUniform(uniformName).set(matrix);
        }
    }

    public List<PostPass> getPasses()
    {
        return passes;
    }


    @Override
    public void _setUniform(String uniformName, float[] floats) {
        for (PostPass postpass : this.passes) {
            postpass.getEffect().safeGetUniform(uniformName).set(floats);
        }
    }
}
