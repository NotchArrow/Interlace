package com.notcharrow.interlace.helper;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;

import java.util.List;

public class Color {

	public Formatting formattingColor;
	public String colorName;
	public Item bannerItem;
	public Item dyeItem;

	private Color(String colorName) {
		this.colorName = colorName;
		switch (colorName) {
			case "White" -> {
				this.formattingColor = Formatting.WHITE;
				this.bannerItem = Items.WHITE_BANNER;
				this.dyeItem = Items.WHITE_DYE;
			}
			case "Light Gray" -> {
				this.formattingColor = Formatting.GRAY;
				this.bannerItem = Items.LIGHT_GRAY_BANNER;
				this.dyeItem = Items.LIGHT_GRAY_DYE;
			}
			case "Gray" -> {
				this.formattingColor = Formatting.DARK_GRAY;
				this.bannerItem = Items.GRAY_BANNER;
				this.dyeItem = Items.GRAY_DYE;
			}
			case "Black" -> {
				this.formattingColor = Formatting.BLACK;
				this.bannerItem = Items.BLACK_BANNER;
				this.dyeItem = Items.BLACK_DYE;
			}
			case "Brown" -> {
				this.formattingColor = Formatting.DARK_GRAY;
				this.bannerItem = Items.BROWN_BANNER;
				this.dyeItem = Items.BROWN_DYE;
			}
			case "Red" -> {
				this.formattingColor = Formatting.RED;
				this.bannerItem = Items.RED_BANNER;
				this.dyeItem = Items.RED_DYE;
			}
			case "Orange" -> {
				this.formattingColor = Formatting.GOLD;
				this.bannerItem = Items.ORANGE_BANNER;
				this.dyeItem = Items.ORANGE_DYE;
			}
			case "Yellow" -> {
				this.formattingColor = Formatting.YELLOW;
				this.bannerItem = Items.YELLOW_BANNER;
				this.dyeItem = Items.YELLOW_DYE;
			}
			case "Lime" -> {
				this.formattingColor = Formatting.GREEN;
				this.bannerItem = Items.LIME_BANNER;
				this.dyeItem = Items.LIME_DYE;
			}
			case "Green" -> {
				this.formattingColor = Formatting.DARK_GREEN;
				this.bannerItem = Items.GREEN_BANNER;
				this.dyeItem = Items.GREEN_DYE;
			}
			case "Cyan" -> {
				this.formattingColor = Formatting.DARK_AQUA;
				this.bannerItem = Items.CYAN_BANNER;
				this.dyeItem = Items.CYAN_DYE;
			}
			case "Light Blue" -> {
				this.formattingColor = Formatting.BLUE;
				this.bannerItem = Items.LIGHT_BLUE_BANNER;
				this.dyeItem = Items.LIGHT_BLUE_DYE;
			}
			case "Blue" -> {
				this.formattingColor = Formatting.DARK_BLUE;
				this.bannerItem = Items.BLUE_BANNER;
				this.dyeItem = Items.BLUE_DYE;
			}
			case "Purple" -> {
				this.formattingColor = Formatting.DARK_PURPLE;
				this.bannerItem = Items.PURPLE_BANNER;
				this.dyeItem = Items.PURPLE_DYE;
			}
			case "Magenta" -> {
				this.formattingColor = Formatting.LIGHT_PURPLE;
				this.bannerItem = Items.MAGENTA_BANNER;
				this.dyeItem = Items.MAGENTA_DYE;
			}
			case "Pink" -> {
				this.formattingColor = Formatting.GRAY;
				this.bannerItem = Items.PINK_BANNER;
				this.dyeItem = Items.PINK_DYE;
			}
		}
	}

	public static List<Color> getColorList() {
		return List.of(
				new Color("White"),
				new Color("Light Gray"),
				new Color("Gray"),
				new Color("Black"),
				new Color("Brown"),
				new Color("Red"),
				new Color("Orange"),
				new Color("Yellow"),
				new Color("Lime"),
				new Color("Green"),
				new Color("Cyan"),
				new Color("Light Blue"),
				new Color("Blue"),
				new Color("Purple"),
				new Color("Magenta"),
				new Color("Pink")
		);
	}
}
