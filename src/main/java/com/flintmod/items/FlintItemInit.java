package com.flintmod.items;

import com.flintmod.main.Config;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FlintItemInit 
{
	public static final Item.ToolMaterial FLINT = EnumHelper.addToolMaterial("FLINT", Config.FLINT_HARVEST_LEVEL, Config.FLINT_DURABILITY, 2.0F, 0.0F, 15);
	
	public static Item flintToolHead;
	public static Item flintSword;
	public static Item flintShovel;
	public static Item flintHoe;
	public static Item flintAxe;
	public static Item flintPick;
	
	public static void mainRegistry()
	{
		initItems();
		registerItems();
	}

	private static void initItems() 
	{
		flintToolHead = new Item().setUnlocalizedName("toolPartFlint").setRegistryName("toolPartFlint");
		flintSword = new FlintKnife(FLINT, "knifeFlint");
		flintHoe = new FlintHoe(FLINT, "hoeFlint");
		flintShovel = new FlintSpade(FLINT, "spadeFlint");
		flintAxe = new FlintAxe(FLINT, "axeFlint");
		flintPick = new FlintPickaxe(FLINT, "pickaxeFlint");
	}
	
	private static void registerItems()
	{
		register(flintToolHead);
		register(flintSword);
		register(flintShovel);
		register(flintAxe);
		register(flintPick);
		register(flintHoe);
	}

	private static void register(Item i) 
	{
		GameRegistry.register(i);
	}
}
