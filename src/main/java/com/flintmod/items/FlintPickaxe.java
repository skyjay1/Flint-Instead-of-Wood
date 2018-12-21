package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FlintPickaxe extends ItemPickaxe
{
	public FlintPickaxe(ToolMaterial m, String name) 
	{
		super(m);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
	}
}
