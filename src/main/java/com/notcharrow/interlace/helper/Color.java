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
			case "white" -> {
				this.formattingColor = Formatting.WHITE;
				this.bannerItem = Items.WHITE_BANNER;
				this.dyeItem = Items.WHITE_DYE;
			}
			case "light_gray" -> {
				this.formattingColor = Formatting.GRAY;
				this.bannerItem = Items.LIGHT_GRAY_BANNER;
				this.dyeItem = Items.LIGHT_GRAY_DYE;
			}
			case "gray" -> {
				this.formattingColor = Formatting.DARK_GRAY;
				this.bannerItem = Items.GRAY_BANNER;
				this.dyeItem = Items.GRAY_DYE;
			}
			case "black" -> {
				this.formattingColor = Formatting.BLACK;
				this.bannerItem = Items.BLACK_BANNER;
				this.dyeItem = Items.BLACK_DYE;
			}
			case "brown" -> {
				this.formattingColor = Formatting.DARK_GRAY;
				this.bannerItem = Items.BROWN_BANNER;
				this.dyeItem = Items.BROWN_DYE;
			}
			case "red" -> {
				this.formattingColor = Formatting.RED;
				this.bannerItem = Items.RED_BANNER;
				this.dyeItem = Items.RED_DYE;
			}
			case "orange" -> {
				this.formattingColor = Formatting.GOLD;
				this.bannerItem = Items.ORANGE_BANNER;
				this.dyeItem = Items.ORANGE_DYE;
			}
			case "yellow" -> {
				this.formattingColor = Formatting.YELLOW;
				this.bannerItem = Items.YELLOW_BANNER;
				this.dyeItem = Items.YELLOW_DYE;
			}
			case "lime" -> {
				this.formattingColor = Formatting.GREEN;
				this.bannerItem = Items.LIME_BANNER;
				this.dyeItem = Items.LIME_DYE;
			}
			case "green" -> {
				this.formattingColor = Formatting.DARK_GREEN;
				this.bannerItem = Items.GREEN_BANNER;
				this.dyeItem = Items.GREEN_DYE;
			}
			case "cyan" -> {
				this.formattingColor = Formatting.DARK_AQUA;
				this.bannerItem = Items.CYAN_BANNER;
				this.dyeItem = Items.CYAN_DYE;
			}
			case "light_blue" -> {
				this.formattingColor = Formatting.BLUE;
				this.bannerItem = Items.LIGHT_BLUE_BANNER;
				this.dyeItem = Items.LIGHT_BLUE_DYE;
			}
			case "blue" -> {
				this.formattingColor = Formatting.DARK_BLUE;
				this.bannerItem = Items.BLUE_BANNER;
				this.dyeItem = Items.BLUE_DYE;
			}
			case "purple" -> {
				this.formattingColor = Formatting.DARK_PURPLE;
				this.bannerItem = Items.PURPLE_BANNER;
				this.dyeItem = Items.PURPLE_DYE;
			}
			case "magenta" -> {
				this.formattingColor = Formatting.LIGHT_PURPLE;
				this.bannerItem = Items.MAGENTA_BANNER;
				this.dyeItem = Items.MAGENTA_DYE;
			}
			case "pink" -> {
				this.formattingColor = Formatting.GRAY;
				this.bannerItem = Items.PINK_BANNER;
				this.dyeItem = Items.PINK_DYE;
			}
		}
	}

	public static List<Color> getColorList() {
		return List.of(
				new Color("white"),
				new Color("light_gray"),
				new Color("gray"),
				new Color("black"),
				new Color("brown"),
				new Color("red"),
				new Color("orange"),
				new Color("yellow"),
				new Color("lime"),
				new Color("green"),
				new Color("cyan"),
				new Color("light_blue"),
				new Color("blue"),
				new Color("purple"),
				new Color("magenta"),
				new Color("pink")
		);
	}
}
