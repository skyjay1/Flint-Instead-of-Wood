package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FlintPickaxe extends ItemPickaxe
{
	public FlintPickaxe(String name) 
	{
		super(FlintItems.FLINT);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}
}
