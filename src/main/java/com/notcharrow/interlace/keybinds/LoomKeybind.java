package com.notcharrow.interlace.keybinds;

import com.notcharrow.interlace.Interlace;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.notcharrow.interlace.keybinds.KeybindRegistry.loomKeybind;

public class LoomKeybind {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || !loomKeybind.isPressed()) {
                return;
            }

            MutableText title = Text.translatable("container.interlace.editor");
            Style style = Style.EMPTY.withFormatting(Formatting.BOLD);
            title.setStyle(style);

            LoomScreenHandler screenHandler = new ExtendedLoomScreenHandler(
                    client.player.currentScreenHandler.syncId,
                    client.player.getInventory());

            ExtendedLoomScreen screen = new ExtendedLoomScreen(
                    screenHandler,
                    client.player.getInventory(),
                    title);

            client.setScreen(screen);
        });
    }

    public static class ExtendedLoomScreen extends LoomScreen {

        private static final int COLUMNS = 4;
        private final List<FakeSlot> fakeSlots = new ArrayList<>();

        public ExtendedLoomScreen(LoomScreenHandler handler, PlayerInventory inventory, Text title) {
            super(handler, inventory, title);
        }

        @Override
        protected void init() {
            super.init();
            fakeSlots.clear();
            fillSlots();
            addBannerSlots();
            addDyeSlots();
            addOutputButton();
            addSaveButton();
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);

            for (FakeSlot slot : fakeSlots) {
                this.drawSlot(context, slot);
            }

            for (FakeSlot slot : fakeSlots) {
                if (this.isPointWithinBounds(slot.x-this.x, slot.y-this.y, 16, 16, mouseX, mouseY)) {
                    context.drawItemTooltip(
                            this.textRenderer,
                            slot.getStack(),
                            mouseX,
                            mouseY
                    );
                }
            }
        }

        @Override
        public boolean mouseClicked(Click click, boolean doubled) {
            if (click.button() == 0) {
                for (FakeSlot slot : fakeSlots) {
                    if (this.isPointWithinBounds(slot.x-this.x, slot.y-this.y, 16, 16, click.x(), click.y())) {
                        slot.slot().setStack(slot.getStack().copy());
                        return true;
                    }
                }
            }
            return super.mouseClicked(click, doubled);
        }

        private void fillSlots() {
            Slot bannerSlot = this.handler.getBannerSlot();
            Slot dyeSlot = this.handler.getDyeSlot();

            if (bannerSlot.getStack().isEmpty()) {
                bannerSlot.setStack(new ItemStack(Items.WHITE_BANNER));
            }
            if (dyeSlot.getStack().isEmpty()) {
                dyeSlot.setStack(new ItemStack(Items.WHITE_DYE));
            }
        }

        private void addBannerSlots() {
            final int SIZE = getButtonSize();
            Slot bannerSlot = this.handler.getBannerSlot();

            int startX = this.x - (COLUMNS * SIZE) - 4;
            int startY = this.y;

            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                int col = i % COLUMNS;
                int row = i / COLUMNS;

                ItemStack stack = PaintBucket.maybeBuyFromTheRegistriesStore(color).bannerStack();
                fakeSlots.add(new FakeSlot(
                        startX + col * SIZE,
                        startY + row * SIZE,
                        stack
                ) {
                    @Override
                    public Slot slot() {
                        return bannerSlot;
                    }
                });
                i++;
            }
        }

        private void addDyeSlots() {
            final int SIZE = getButtonSize();
            Slot dyeSlot = this.handler.getDyeSlot();

            int startX = this.x - (COLUMNS * SIZE) - 4;
            int startY = this.y + ((DyeColor.values().length + COLUMNS - 1) / COLUMNS) * SIZE + 6;

            int i = 0;
            for (DyeColor color : DyeColor.values()) {
                int col = i % COLUMNS;
                int row = i / COLUMNS;

                ItemStack stack = PaintBucket.maybeBuyFromTheRegistriesStore(color).dyeStack();
                fakeSlots.add(new FakeSlot(
                        startX + col * SIZE,
                        startY + row * SIZE,
                        stack
                ) {
                    @Override
                    public Slot slot() {
                        return dyeSlot;
                    }
                });
                i++;
            }
        }

        private void addOutputButton() {
            int size = getButtonSize();
            Slot outputSlot = this.handler.getOutputSlot();
            Slot bannerSlot = this.handler.getBannerSlot();

            this.addDrawableChild(new TexturedButtonWidget(
                    this.x + outputSlot.x,
                    this.y + outputSlot.y,
                    size,
                    size,
                    new ButtonTextures(Identifier.of("interlace", "output")),
                    button -> {
                        ItemStack output = outputSlot.getStack();
                        if (!output.isEmpty() && !ItemStack.areEqual(output, bannerSlot.getStack())) {
                            bannerSlot.setStack(output.copy());
                        }
                    }
            ));
        }

        private int getButtonSize() {
            return Math.abs(this.handler.getDyeSlot().x - this.handler.getBannerSlot().x);
        }

        private void addSaveButton() {
            Slot outputSlot = this.handler.getOutputSlot();

            int x = this.x + this.backgroundWidth + 2;
            int y = this.y + 2;
            this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("container.interlace.editor.save"), btn -> {
                var stack = outputSlot.getStack();
                var comp = stack.get(DataComponentTypes.BANNER_PATTERNS);
                if (!stack.isEmpty() && comp != null && !comp.layers().isEmpty()) Interlace.BANNERS.saveBanner(stack.copy());
            }).position(x, y).width(this.width/5).build());
        }
    }

    public static class ExtendedLoomScreenHandler extends LoomScreenHandler implements Extended {
        public ExtendedLoomScreenHandler(int syncId, PlayerInventory playerInventory) {
            super(syncId, playerInventory);
        }
    }

    public interface Extended {}
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
                    if (banner == null && item instanceof BlockItem blockItem &&
                            blockItem.getBlock() instanceof AbstractBannerBlock bannerBlock &&
                            bannerBlock.getColor() == color) {
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

    private abstract static class FakeSlot extends Slot {
        private ItemStack stack;

        public FakeSlot(int x, int y, ItemStack stack) {
            super(null, 0, x, y);
            this.stack = stack;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public boolean canTakeItems(PlayerEntity player) {
            return false;
        }

        @Override
        public ItemStack getStack() {
            return stack;
        }

        public void setStack(ItemStack stack) {
            this.stack = stack;
        }

        public abstract Slot slot();
    }
}
