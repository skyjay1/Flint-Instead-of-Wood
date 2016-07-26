package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.item.ItemSword;

public class FlintKnife extends ItemSword
{

	public FlintKnife(ToolMaterial material, String name) 
	{
		super(material);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
	}

}
