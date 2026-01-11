package com.notcharrow.interlace.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface ScreenCoordinateAccessor {
	@Accessor("x")
	int interlace$getX();

	@Accessor("y")
	int interlace$getY();
}
