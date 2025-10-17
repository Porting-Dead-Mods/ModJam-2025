package com.portingdeadmods.modjam.compat.guideme;

import com.portingdeadmods.modjam.Modjam;
import guideme.Guide;
import guideme.GuidesCommon;
import guideme.scene.element.SceneElementTagCompiler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public final class GuideMECompat {
    public static final ResourceLocation ID = Modjam.rl("guide");

    public static void init() {
        Guide.builder(ID)
                .extension(SceneElementTagCompiler.EXTENSION_POINT, new MultiblockShapeCompiler())
                .build();
    }

    public static void openGuide(Player player) {
        GuidesCommon.openGuide(player, ID);
    }
}
