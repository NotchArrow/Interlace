package com.notcharrow.interlace;

import com.notcharrow.interlace.config.SavedBanners;
import com.notcharrow.interlace.keybinds.KeybindRegistry;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Interlace implements ClientModInitializer {
    public static final String MOD_ID = "interlace";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final SavedBanners BANNERS = new SavedBanners();

	@Override
	public void onInitializeClient() {
		KeybindRegistry.registerKeybinds();
        BANNERS.init();
	}
}
