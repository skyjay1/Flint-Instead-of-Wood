package com.flintmod.main;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class FlintConfig {
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final FlintConfig.ConfigItems ITEMS = new FlintConfig.ConfigItems(BUILDER);
	public static final FlintConfig.ConfigWorld WORLD = new FlintConfig.ConfigWorld(BUILDER);

	public static class ConfigItems {
		public final ForgeConfigSpec.BooleanValue ENABLE_SWORD;
		public final ForgeConfigSpec.BooleanValue ENABLE_PICK;
		public final ForgeConfigSpec.BooleanValue ENABLE_HOE;
		public final ForgeConfigSpec.BooleanValue ENABLE_AXE;
		public final ForgeConfigSpec.BooleanValue ENABLE_SHOVEL;
		public final ForgeConfigSpec.IntValue FLINT_TIER;
		public final ForgeConfigSpec.IntValue FLINT_DURABILITY;
		
		public ConfigItems(Builder builder) {
			builder.push("items");
			ENABLE_AXE = builder
					.comment("Whether or not the flint axe has a crafting recipe", 
							"ENABLE THIS if wooden tools are disabled!")
					.define("Enable Flint Axe", true);
			ENABLE_SWORD = builder
				.comment("Whether the flint knife is enabled")
				.define("Enable Flint Sword", true);
			ENABLE_PICK = builder
					.comment("Whether the flint pickaxe is enabled")
					.define("Enable Flint Pick", true);
			ENABLE_HOE = builder
					.comment("Whether the flint hoe is enabled")
					.define("Enable Flint Hoe", true);
			ENABLE_SHOVEL = builder
					.comment("Whether or not the flint spade is enabled")
					.define("Enable Flint Spade", true);
			FLINT_TIER = builder
					.comment("Harvest level of flint tools", 
							"0=wooden, 1=stone")
					.defineInRange("Flint Harvest Level", 0, 0, 1);
			FLINT_DURABILITY = builder
					.comment("Durability of flint tools", 
							"wooden=59, stone=131, iron=250")
					.defineInRange("Flint Tool Durability", 59, 1, 250);
			
			builder.pop();
		}
		
	}

	public static class ConfigWorld {

		
		public final ForgeConfigSpec.BooleanValue REMOVE_WOOD_RECIPES;
		public final ForgeConfigSpec.BooleanValue REMOVE_STONE_RECIPES;
		public final ForgeConfigSpec.BooleanValue REPLACE_IN_LOOT_CHESTS_WOODEN;
		public final ForgeConfigSpec.BooleanValue REPLACE_IN_LOOT_CHESTS_STONE;
		public final ForgeConfigSpec.BooleanValue DISABLE_BREAKING_WOOD;
		public final ForgeConfigSpec.BooleanValue HURT_WHEN_BREAKING_WOOD;
		public final ForgeConfigSpec.IntValue CHANCE_DROP_STICKS;
		
		public ConfigWorld(Builder builder) {
			builder.push("world");
			
			REMOVE_WOOD_RECIPES = builder
					.comment("When true, removes crafting recipes for wooden tools where flint ones are enabled")
					.define("Remove Wood Recipes", true);
			REMOVE_STONE_RECIPES = builder
					.comment("When true, removes crafting recipes for stone tools where flint ones are enabled")
					.define("Remove Stone Recipes", true);
			REPLACE_IN_LOOT_CHESTS_WOODEN = builder
					.comment("When true, loot chests will replace wooden tools with flint tools")
					.define("Replace Wooden in Loot Chests", true);
			REPLACE_IN_LOOT_CHESTS_STONE = builder
					.comment("When true, loot chests will replace stone tools with flint tools")
					.define("Replace Stone in Loot Chests", true);
			DISABLE_BREAKING_WOOD = builder
					.comment("When true, logs may only be harvested using an axe")
					.define("Disable Breaking Logs", false);
			HURT_WHEN_BREAKING_WOOD = builder
					.comment("When true, breaking a log by hand will hurt the player", 
							"Will not trigger if logs can't be harvested")
					.define("Log Harvesting Hurts", true);
			CHANCE_DROP_STICKS = builder
					.comment("Percent chance for sticks to drop from broken leaves",
						"Set to 0 to disable")
					.defineInRange("Stick Drop Chance", 30, 0, 100);
			
			builder.pop();
		}

	}
}
