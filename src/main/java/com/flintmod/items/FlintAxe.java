package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

public class FlintAxe extends ItemAxe
{
	public FlintAxe(String name) 
	{
		super(FlintItems.FLINT, 6.0F, -3.2F);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}
}
