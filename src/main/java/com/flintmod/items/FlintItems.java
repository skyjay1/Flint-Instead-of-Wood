package com.flintmod.items;

import com.flintmod.main.Config;
import com.flintmod.main.FlintModInit;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FlintItems
{
	public static final Item.ToolMaterial FLINT = EnumHelper.addToolMaterial("FLINT", Config.FLINT_HARVEST_LEVEL, Config.FLINT_DURABILITY, 2.0F, 0.0F, 15);
	
	@GameRegistry.ObjectHolder(FlintModInit.MODID + ":tool_part_flint")
	public static Item FLINT_TOOL_HEAD;
	
	@GameRegistry.ObjectHolder(FlintModInit.MODID + ":knife_flint")
	public static Item FLINT_SWORD;
	
	@GameRegistry.ObjectHolder(FlintModInit.MODID + ":spade_flint")
	public static Item FLINT_SHOVEL;
	
	@GameRegistry.ObjectHolder(FlintModInit.MODID + ":hoe_flint")
	public static Item FLINT_HOE;
	
	@GameRegistry.ObjectHolder(FlintModInit.MODID + ":axe_flint")
	public static Item FLINT_AXE;
	
	@GameRegistry.ObjectHolder(FlintModInit.MODID + ":pickaxe_flint")
	public static Item FLINT_PICK;
}
