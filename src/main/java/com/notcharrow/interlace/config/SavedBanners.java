package com.notcharrow.interlace.config;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult;
import com.notcharrow.interlace.Interlace;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SavedBanners {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(Interlace.MOD_ID + ".json");

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<ItemStack> banners = new ArrayList<>();

    public void init() {
        try {
            if (!Files.exists(PATH)) {
                Files.createFile(PATH);
            }

            loadBanners();
        } catch (IOException e) {
            Interlace.LOGGER.error("Failed to initialize banners", e);
        }
    }

    public void saveBanner(ItemStack stack) {
        banners.add(stack.copy());
        saveAllBanners();
    }

    private void saveAllBanners() {
        JsonArray array = new JsonArray();

        for (ItemStack stack : banners) {
            DataResult<JsonElement> result = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack);
            result.result().ifPresent(array::add);
            result.error().ifPresent(err ->
                    Interlace.LOGGER.error("Failed to encode ItemStack: {}", err.message())
            );
        }

        try {
            Files.writeString(PATH, gson.toJson(array));
        } catch (IOException e) {
            Interlace.LOGGER.error("Failed to save banners file", e);
        }
    }

    private void loadBanners() {
        banners.clear();
        if (!Files.exists(PATH)) return;

        try {
            String content = Files.readString(PATH);
            JsonElement element = JsonParser.parseString(content);
            if (element.isJsonArray()) {
                for (JsonElement el : element.getAsJsonArray()) {
                    DataResult<ItemStack> decoded = ItemStack.CODEC.parse(JsonOps.INSTANCE, el);
                    decoded.result().ifPresent(banners::add);
                    decoded.error().ifPresent(err ->
                            Interlace.LOGGER.error("Failed to decode ItemStack: {}", err.message())
                    );
                }
            }
        } catch (IOException e) {
            Interlace.LOGGER.error("Failed to read banners file", e);
        }
    }

    public List<ItemStack> getBanners() {
        return new ArrayList<>(banners);
    }
}
