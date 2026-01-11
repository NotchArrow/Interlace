package com.notcharrow.interlace.mixin;

import com.notcharrow.interlace.keybinds.InterlaceLoomHacks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoomScreen.class)
public class LoomScreenMixin extends ScreenMixin implements InterlaceLoomHacks {
    @Unique
    private Runnable interlace$refresher = () -> {};

    @Override
    public void interlace$onScreenRefreshed(Runnable runnable) {
        this.interlace$refresher = runnable;
    }

    @Override
    protected void refresh(CallbackInfo ci) {
        this.interlace$refresher.run();
    }
}
