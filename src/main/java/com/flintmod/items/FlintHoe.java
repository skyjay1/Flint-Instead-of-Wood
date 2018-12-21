package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;

public class FlintHoe extends ItemHoe
{
	public FlintHoe(String name) 
	{
		super(FlintItems.FLINT);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}
}
