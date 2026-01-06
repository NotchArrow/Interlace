package com.notcharrow.interlace.keybinds;

import com.notcharrow.interlace.helper.Color;
import com.notcharrow.interlace.mixin.ScreenCoordinateAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static com.notcharrow.interlace.keybinds.KeybindRegistry.loomKeybind;

public class LoomKeybind {
	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null && loomKeybind.isPressed()) {

				// System.out.println(client.player.getMainHandStack().getItem().getComponents().get(DataComponentTypes.BANNER_PATTERNS));

				MutableText title = Text.literal("Banner Editor");
				Style style = Style.EMPTY.withFormatting(Formatting.BOLD);
				title.setStyle(style);

				LoomScreenHandler screenHandler = new LoomScreenHandler(
						client.player.currentScreenHandler.syncId,
						client.player.getInventory());
				LoomScreen screen = new LoomScreen(
						screenHandler,
						client.player.getInventory(),
						title);
				client.setScreen(screen);

				fillSlots(screen);

				drawBannerButtons(screen);
				drawDyeButtons(screen);
				drawOutputButton(screen);
			}
		});
	}

	private static void fillSlots(LoomScreen screen) {
		Slot bannerSlot = screen.getScreenHandler().getBannerSlot();
		Slot dyeSlot = screen.getScreenHandler().getDyeSlot();

		if (bannerSlot.getStack().isEmpty()) {
			bannerSlot.setStack(new ItemStack(Items.WHITE_BANNER));
		}
		if (dyeSlot.getStack().isEmpty()) {
			dyeSlot.setStack(new ItemStack(Items.RED_DYE));
		}
	}

	private static void drawBannerButtons(LoomScreen screen) {
		final int BUTTON_SIZE = getButtonSize(screen);
		Slot bannerSlot = screen.getScreenHandler().getBannerSlot();

		int i = 0;
		int x = ((ScreenCoordinateAccessor) screen).getX() - BUTTON_SIZE * 5;
		int y = ((ScreenCoordinateAccessor) screen).getY();

		for (Color color : Color.getColorList()) {
			if (i % 4 == 0 && i != 0) {
				x = ((ScreenCoordinateAccessor) screen).getX() - BUTTON_SIZE * 5;
				y += BUTTON_SIZE;
			}
			Screens.getButtons(screen).add(new TexturedButtonWidget(
				x, y, BUTTON_SIZE, BUTTON_SIZE,
				new ButtonTextures(Identifier.of("interlace", "banners/" + color.colorName + "_banner")),
				button -> {
					bannerSlot.setStack(new ItemStack(color.bannerItem));
				}
			));
			x += BUTTON_SIZE;
			i++;
		}
	}

	private static void drawDyeButtons(LoomScreen screen) {
		final int BUTTON_SIZE = getButtonSize(screen);
		Slot dyeSlot = screen.getScreenHandler().getDyeSlot();

		int i = 0;
		int x = ((ScreenCoordinateAccessor) screen).getX() - BUTTON_SIZE * 5;
		int y = ((ScreenCoordinateAccessor) screen).getY() + BUTTON_SIZE * 5;

		for (Color color : Color.getColorList()) {
			if (i % 4 == 0 && i != 0) {
				x = ((ScreenCoordinateAccessor) screen).getX() - BUTTON_SIZE * 5;
				y += BUTTON_SIZE;
			}
			Screens.getButtons(screen).add(new TexturedButtonWidget(
					x, y, BUTTON_SIZE, BUTTON_SIZE,
					new ButtonTextures(Identifier.of("interlace", "dyes/" + color.colorName + "_dye")),
					button -> {
						dyeSlot.setStack(new ItemStack(color.dyeItem));
					}
			));
			x += BUTTON_SIZE;
			i++;
		}
	}

	private static void drawOutputButton(LoomScreen screen) {
		int BUTTON_SIZE = getButtonSize(screen);
		Slot outputSlot = screen.getScreenHandler().getOutputSlot();
		Slot bannerSlot = screen.getScreenHandler().getBannerSlot();

		int x = (((ScreenCoordinateAccessor) screen).getX() + outputSlot.x);
		int y = (((ScreenCoordinateAccessor) screen).getY() + outputSlot.y);

		Screens.getButtons(screen).add(new TexturedButtonWidget(
				x, y, BUTTON_SIZE, BUTTON_SIZE,
				new ButtonTextures(Identifier.of("interlace", "output")),
				button -> {
					ItemStack output = outputSlot.getStack();
					if (!output.equals(bannerSlot.getStack()) && !output.isEmpty()) {
						bannerSlot.setStack(output);

						// System.out.println(output.getItem().getTranslationKey());
						// System.out.println(banner.getColor());
						// System.out.println(bannerSlot.getStack().getComponents().get(DataComponentTypes.BANNER_PATTERNS));
					}
				}
		));
	}

	private static int getButtonSize(LoomScreen screen) {
		return Math.abs(screen.getScreenHandler().getDyeSlot().x - screen.getScreenHandler().getBannerSlot().x);
	}
}
