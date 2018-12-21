package com.flintmod.items;

import com.flintmod.main.Config;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FlintItemInit 
{
	public static final Item.ToolMaterial FLINT = EnumHelper.addToolMaterial("FLINT", Config.FLINT_HARVEST_LEVEL, Config.FLINT_DURABILITY, 2.0F, 0.0F, 15);
	
	public static Item FLINT_TOOL_HEAD;
	public static Item FLINT_SWORD;
	public static Item FLINT_SHOVEL;
	public static Item FLINT_HOE;
	public static Item FLINT_AXE;
	public static Item FLINT_PICK;
	
	public static void mainRegistry()
	{
		initItems();
		registerItems();
	}

	private static void initItems() 
	{
		FLINT_TOOL_HEAD = new Item().setUnlocalizedName("tool_part_flint").setRegistryName("tool_part_flint");
		FLINT_SWORD = new FlintKnife(FLINT, "knife_flint");
		FLINT_HOE = new FlintHoe(FLINT, "hoe_flint");
		FLINT_SHOVEL = new FlintSpade(FLINT, "spade_flint");
		FLINT_AXE = new FlintAxe(FLINT, "axe_flint");
		FLINT_PICK = new FlintPickaxe(FLINT, "pickaxe_flint");
	}
	
	private static void registerItems()
	{
		register(FLINT_TOOL_HEAD);
		register(FLINT_SWORD);
		register(FLINT_SHOVEL);
		register(FLINT_AXE);
		register(FLINT_PICK);
		register(FLINT_HOE);
	}

	private static void register(Item i) 
	{
		GameRegistry.register(i);
	}
}
