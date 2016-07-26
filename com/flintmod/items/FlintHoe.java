package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.item.ItemHoe;

public class FlintHoe extends ItemHoe
{
	public FlintHoe(ToolMaterial material, String name) 
	{
		super(material);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
	}
}
