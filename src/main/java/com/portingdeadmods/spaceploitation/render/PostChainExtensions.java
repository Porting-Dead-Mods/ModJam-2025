package com.portingdeadmods.spaceploitation.render;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.joml.Matrix4f;

import java.util.List;

public interface PostChainExtensions {

    public List<PostPass> getPasses();

    void _setUniform(String uniformName, Matrix4f matrix);

    void _setUniform(String uniformName, float[] floats);



    static void setUniform(PostChain chain, String uniformName, Matrix4f matrix) {
        ((PostChainExtensions) (Object) chain)._setUniform(uniformName, matrix);
    }

    static void setUniform(PostChain chain, String uniformName, float[] matrix) {
        ((PostChainExtensions) (Object) chain)._setUniform(uniformName, matrix);
    }

}
