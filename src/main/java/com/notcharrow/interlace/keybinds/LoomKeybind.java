package com.notcharrow.interlace.keybinds;

import com.notcharrow.interlace.helper.Color;
import com.notcharrow.interlace.helper.Pattern;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import static com.notcharrow.interlace.keybinds.KeybindRegistry.loomKeybind;

public class LoomKeybind {
	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 20;

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && loomKeybind.isPressed()) {

				LoomScreenHandler screenHandler = new LoomScreenHandler(
						client.player.currentScreenHandler.syncId,
						client.player.getInventory());
				LoomScreen screen = new LoomScreen(
						screenHandler,
						client.player.getInventory(),
						Text.of("Banner Editor"));
				client.setScreen(screen);

				Slot bannerSlot = screenHandler.getBannerSlot();
				Slot dyeSlot = screenHandler.getDyeSlot();
				Slot patternSlot = screenHandler.getPatternSlot();
				Slot outputSlot = screenHandler.getOutputSlot();

				if (bannerSlot.getStack().isEmpty()) {
					bannerSlot.setStack(new ItemStack(Items.WHITE_BANNER));
				}

				if (dyeSlot.getStack().isEmpty()) {
					dyeSlot.setStack(new ItemStack(Items.RED_DYE));
				}

				Screens.getButtons(screen).add(ButtonWidget.builder(Text.literal("Take Output"), button -> {
					bannerSlot.setStack(outputSlot.getStack());
				}).dimensions(10, 10, BUTTON_WIDTH, BUTTON_HEIGHT).build());

				for (int i = 1; i <= 3; i++) {
					int x = BUTTON_WIDTH * i + 10;
					int y = 10;
					if (i == 3) {
						x = 700;
						for (Pattern pattern: Pattern.getPatternList()) {
							Screens.getButtons(screen).add(ButtonWidget.builder(Text.literal(pattern.patternName), button -> {
								patternSlot.setStack(new ItemStack(pattern.patternItem));
							}).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
							y += BUTTON_HEIGHT;
						}
					} else {
						for (Color color : Color.getColorList()) {
							if (i == 1) {
								MutableText buttonText = Text.literal(color.colorName + " Banner");
								buttonText.setStyle(Style.EMPTY.withColor(color.formattingColor));
								Screens.getButtons(screen).add(ButtonWidget.builder(buttonText, button -> {
									bannerSlot.setStack(new ItemStack(color.bannerItem));
								}).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
							} else {
								MutableText buttonText = Text.literal(color.colorName + " Dye");
								buttonText.setStyle(Style.EMPTY.withColor(color.formattingColor));
								Screens.getButtons(screen).add(ButtonWidget.builder(buttonText, button -> {
									dyeSlot.setStack(new ItemStack(color.dyeItem));
								}).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
							}
							y += BUTTON_HEIGHT;
						}
					}
				}
			}
		});
	}
}
