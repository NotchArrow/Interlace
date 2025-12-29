package com.notcharrow.interlace.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import static com.notcharrow.interlace.keybinds.KeybindRegistry.loomKeybind;

public class LoomKeybind {

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && loomKeybind.isPressed()) {
				System.out.println("yay");
			}
		});
	}
}
