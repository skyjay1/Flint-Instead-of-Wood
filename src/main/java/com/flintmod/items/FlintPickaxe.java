package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;

public class FlintPickaxe extends ItemPickaxe {
	
	public FlintPickaxe(final String name, final Item.Properties properties) {
		super(FlintItemInit.FLINT, 1, -2.8F, properties.maxStackSize(1));
		this.setRegistryName(FlintModInit.MODID, name);
	}
}
