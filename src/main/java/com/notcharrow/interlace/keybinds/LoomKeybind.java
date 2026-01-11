package com.notcharrow.interlace.keybinds;

import com.notcharrow.interlace.mixin.ScreenCoordinateAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static com.notcharrow.interlace.keybinds.KeybindRegistry.loomKeybind;

public class LoomKeybind {

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null || !loomKeybind.isPressed()) {
                return;
			}

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

            Runnable specialButtonsAdder = () -> {
                drawBannerButtons(screen);
                drawDyeButtons(screen);
                drawOutputButton(screen);
            };
            specialButtonsAdder.run();
            ((InterlaceLoomHacks) screen).interlace$onScreenRefreshed(specialButtonsAdder);
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
		int x = ((ScreenCoordinateAccessor) screen).interlace$getX() - BUTTON_SIZE * 5;
		int y = ((ScreenCoordinateAccessor) screen).interlace$getY();

		for (DyeColor color : DyeColor.values()) {
			if (i % 4 == 0 && i != 0) {
				x = ((ScreenCoordinateAccessor) screen).interlace$getX() - BUTTON_SIZE * 5;
				y += BUTTON_SIZE;
			}
            ItemStack bannerStack = PaintBucket.maybeBuyFromTheRegistriesStore(color).bannerStack();
            Screens.getButtons(screen).add(new ItemBasedButtonWidget(
				x, y, BUTTON_SIZE, BUTTON_SIZE,
				bannerStack,
				button -> bannerSlot.setStack(bannerStack.copy())
            ));
			x += BUTTON_SIZE;
			i++;
		}
	}

	private static void drawDyeButtons(LoomScreen screen) {
		final int BUTTON_SIZE = getButtonSize(screen);
		Slot dyeSlot = screen.getScreenHandler().getDyeSlot();

		int i = 0;
		int x = ((ScreenCoordinateAccessor) screen).interlace$getX() - BUTTON_SIZE * 5;
		int y = ((ScreenCoordinateAccessor) screen).interlace$getY() + BUTTON_SIZE * 5;

		for (DyeColor color : DyeColor.values()) {
			if (i % 4 == 0 && i != 0) {
				x = ((ScreenCoordinateAccessor) screen).interlace$getX() - BUTTON_SIZE * 5;
				y += BUTTON_SIZE;
			}
            ItemStack dyeStack = PaintBucket.maybeBuyFromTheRegistriesStore(color).dyeStack();
			Screens.getButtons(screen).add(new ItemBasedButtonWidget(
					x, y, BUTTON_SIZE, BUTTON_SIZE,
					dyeStack,
					button -> {
						dyeSlot.setStack(dyeStack.copy());
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

		int x = (((ScreenCoordinateAccessor) screen).interlace$getX() + outputSlot.x);
		int y = (((ScreenCoordinateAccessor) screen).interlace$getY() + outputSlot.y);

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

    public record PaintBucket(Item dye, Item banner) {
        public static final Map<DyeColor, PaintBucket> CONTAINERS = new HashMap<>();
        public static final PaintBucket WHITE = new PaintBucket(Items.WHITE_DYE, Items.WHITE_BANNER);

        public static PaintBucket maybeBuyFromTheRegistriesStore(DyeColor dyeColor) {
            if (!CONTAINERS.containsKey(DyeColor.WHITE)) {
                CONTAINERS.put(DyeColor.WHITE, WHITE);
            }

            PaintBucket result = CONTAINERS.computeIfAbsent(dyeColor, color -> {
                Item dye = null;
                Item banner = null;
                for (Item item : Registries.ITEM) {
                    if (dye != null && banner != null) {
                        return new PaintBucket(dye, banner);
                    }
                    if (dye == null && item instanceof DyeItem dyeItem && dyeItem.getColor() == color) {
                        dye = dyeItem;
                    }
                    if (banner == null && item instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractBannerBlock bannerBlock && bannerBlock.getColor() == color) {
                        banner = blockItem;
                    }
                }
                return null;
            });

            return result != null ? result : WHITE;
        }

        public ItemStack dyeStack() {
            return this.dye.getDefaultStack();
        }

        public ItemStack bannerStack() {
            return this.banner.getDefaultStack();
        }
    }

    public static class ItemBasedButtonWidget extends ButtonWidget {
        protected final ItemStack stack;

        public ItemBasedButtonWidget(int x, int y, int width, int height, ItemStack stack, ButtonWidget.PressAction pressAction) {
            this(x, y, width, height, stack, pressAction, ScreenTexts.EMPTY);
        }

        public ItemBasedButtonWidget(int x, int y, int width, int height, ItemStack stack, ButtonWidget.PressAction pressAction, Text text) {
            super(x, y, width, height, text, pressAction, DEFAULT_NARRATION_SUPPLIER);
            this.stack = stack;
        }

        @SuppressWarnings("unused")
        public ItemBasedButtonWidget(int width, int height, ItemStack stack, ButtonWidget.PressAction pressAction, Text text) {
            this(0, 0, width, height, stack, pressAction, text);
        }

        public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            context.drawItem(this.stack, this.getX(), this.getY());
        }
    }
}
