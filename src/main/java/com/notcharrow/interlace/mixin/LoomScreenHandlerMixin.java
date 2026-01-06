package com.notcharrow.interlace.mixin;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BannerPatternTags;
import net.minecraft.screen.LoomScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LoomScreenHandler.class)
public class LoomScreenHandlerMixin { // temporarily disabled

	@Inject(method = "getPatternsFor", at = @At("HEAD"), cancellable = true)
	private void returnAllPatterns(ItemStack stack, CallbackInfoReturnable<List<RegistryEntry.Reference<BannerPattern>>> cir) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (client.player != null) {
			var registry = client.player.getEntityWorld().getRegistryManager()
					.getOrThrow(RegistryKeys.BANNER_PATTERN);

			List<RegistryEntry.Reference<BannerPattern>> allPatterns = registry.streamEntries()
					.sorted((pattern1, pattern2) -> {
						if (pattern1.isIn(BannerPatternTags.NO_ITEM_REQUIRED) &&
								!pattern2.isIn(BannerPatternTags.NO_ITEM_REQUIRED)) {
							return -1;
						}
						else if (!pattern1.isIn(BannerPatternTags.NO_ITEM_REQUIRED) &&
								pattern2.isIn(BannerPatternTags.NO_ITEM_REQUIRED)) {
							return 1;
						} else {
							return 0;
						}
					})
					.toList();
			
			cir.setReturnValue(allPatterns);
		}
	}
}
