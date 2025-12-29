package com.notcharrow.interlace;

import com.notcharrow.interlace.keybinds.KeybindRegistry;
import net.fabricmc.api.ClientModInitializer;

public class Interlace implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		KeybindRegistry.registerKeybinds();
	}
}
