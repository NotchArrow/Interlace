package com.notcharrow.interlace.helper;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;

public class Pattern {

	public String patternName;
	public Item patternItem;

	private Pattern(String patternName) {
		this.patternName = patternName;
		switch (patternName) {
			case "Clear" -> this.patternItem = Items.AIR;
			case "Bordure Indented" -> this.patternItem = Items.BORDURE_INDENTED_BANNER_PATTERN;
			case "Field Masoned" -> this.patternItem = Items.FIELD_MASONED_BANNER_PATTERN;
			case "Flower Charge" -> this.patternItem = Items.FLOWER_BANNER_PATTERN;
			case "Globe" -> this.patternItem = Items.GLOBE_BANNER_PATTERN;
			case "Creeper Charge" -> this.patternItem = Items.CREEPER_BANNER_PATTERN;
			case "Snout" -> this.patternItem = Items.PIGLIN_BANNER_PATTERN;
			case "Flow" -> this.patternItem = Items.FLOW_BANNER_PATTERN;
			case "Guster" -> this.patternItem = Items.GUSTER_BANNER_PATTERN;
			case "Skull Charge" -> this.patternItem = Items.SKULL_BANNER_PATTERN;
			case "Thing" -> this.patternItem = Items.MOJANG_BANNER_PATTERN;
		}
	}

	public static List<Pattern> getPatternList() {
		return List.of(
				new Pattern("Clear"),
				new Pattern("Bordure Indented"),
				new Pattern("Field Masoned"),
				new Pattern("Flower Charge"),
				new Pattern("Globe"),
				new Pattern("Creeper Charge"),
				new Pattern("Snout"),
				new Pattern("Flow"),
				new Pattern("Guster"),
				new Pattern("Skull Charge"),
				new Pattern("Thing")
		);
	}
}
