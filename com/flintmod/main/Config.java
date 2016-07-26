package com.flintmod.main;

import net.minecraftforge.common.config.Configuration;

public class Config 
{
	public static boolean CRAFT_SWORD;
	public static boolean CRAFT_PICK;
	public static boolean CRAFT_HOE;
	public static boolean CRAFT_AXE;
	public static boolean CRAFT_SHOVEL;
	public static boolean DISABLE_BREAKING_WOOD;
	public static boolean HURT_WHEN_BREAKING_WOOD;
	
	public static boolean REMOVE_WOODEN;
	public static boolean REMOVE_STONE;
	
	public static int CHANCE_DROP_STICKS;
	public static int FLINT_DURABILITY;
	public static int FLINT_HARVEST_LEVEL;
	
	private static final String WORLD = "world";
	private static final String CRAFTING = "crafting";
	
	public static void mainRegistry(Configuration config)
	{
		config.load();
		
		CRAFT_AXE = config.getBoolean("Craft flint axe", CRAFTING, true, 
				"Whether or not the flint axe has a crafting recipe. ENABLE THIS if wooden tools are disabled!");
		CRAFT_HOE = config.getBoolean("Craft flint hoe", CRAFTING, true, 
				"Whether or not the flint hoe has a crafting recipe");
		CRAFT_PICK = config.getBoolean("Craft flint pickaxe", CRAFTING, true, 
				"Whether or not the flint pickaxe has a crafting recipe");
		CRAFT_SHOVEL = config.getBoolean("Craft flint shovel", CRAFTING, true, 
				"Whether or not the flint shovel has a crafting recipe");
		CRAFT_SWORD = config.getBoolean("Craft flint knife", CRAFTING, true, 
				"Whether or not the flint knife has a crafting recipe");
		
		REMOVE_WOODEN = config.getBoolean("Remove Wooden Tools", CRAFTING, true, 
				"When true, removes crafting recipes for all wooden tools");
		REMOVE_STONE = config.getBoolean("Remove Stone Tools", CRAFTING, false, 
				"When true, removes crafting recipes for all stone tools");
		
		CHANCE_DROP_STICKS = config.getInt("Stick drop chance", WORLD, 35, 0, 100, 
				"Percent chance for leaves to drop sticks when broken");
		FLINT_DURABILITY = config.getInt("Flint durability", WORLD, 59, 1, 250, 
				"Base durability of flint tools (wood=59, stone=131, iron=250)");
		FLINT_HARVEST_LEVEL = config.getInt("Flint Harvest Level", WORLD, 0, 0, 2, 
				"Harvest level of flint tools (0=wooden, 1=stone, 2=iron)");
		
		DISABLE_BREAKING_WOOD = config.getBoolean("Disable Breaking Logs", WORLD, false, 
				"When true, logs may only be harvested using an axe");
		HURT_WHEN_BREAKING_WOOD = config.getBoolean("Log Harvesting Hurts", WORLD, true, 
				"When true, breaking a log by hand will hurt the player. Will not trigger if logs can't be harvested.");
		
		config.save();
	}
}
