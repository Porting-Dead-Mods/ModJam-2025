package com.portingdeadmods.modjam.mixin;

import com.portingdeadmods.portingdeadlibs.api.client.screens.widgets.LazyImageButton;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LazyImageButton.class)
public class LazyImageButtonMixin {
    @Inject(method = "onPress", at = @At("HEAD"))
    private void onPress(CallbackInfo ci) {
        LazyImageButton self = (LazyImageButton) (Object) this;
        self.playDownSound(Minecraft.getInstance().getSoundManager());
    }
}
