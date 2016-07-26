package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.item.ItemPickaxe;

public class FlintPickaxe extends ItemPickaxe
{
	// needed an exposed constructor because ItemAxe(ToolMaterial) is protected
	public FlintPickaxe(ToolMaterial m, String name) 
	{
		super(m);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
	}
}
