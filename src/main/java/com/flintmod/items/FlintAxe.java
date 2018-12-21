package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.item.ItemAxe;

public class FlintAxe extends ItemAxe
{
	// needed an exposed constructor because ItemAxe(ToolMaterial) is protected
	public FlintAxe(ToolMaterial m, String name) 
	{
		super(m, 6.0F, -3.2F);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
	}
}
