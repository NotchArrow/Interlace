package com.notcharrow.interlace.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeybindRegistry {
	public static KeyBinding loomKeybind;

	public static void registerKeybinds() {
		loomKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.interlace.loomKeybind",
                GLFW.GLFW_KEY_UNKNOWN,
				KeyBinding.Category.create(Identifier.of("interlace"))
		));
		LoomKeybind.register();
	}
}
