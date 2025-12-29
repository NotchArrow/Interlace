package com.notcharrow.interlace.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class KeybindRegistry {
	public static KeyBinding loomKeybind;

	public static void registerKeybinds() {
		loomKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.interlace.loomKeybind",
				InputUtil.GLFW_KEY_B,
				KeyBinding.Category.create(Identifier.of("interlace"))
		));
		LoomKeybind.register();
	}
}
