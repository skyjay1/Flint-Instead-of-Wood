package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;

public class FlintHoe extends ItemHoe {
	
	public FlintHoe(final String name, final Item.Properties properties) {
		super(FlintItemInit.FLINT, -2.0F, properties.maxStackSize(1));
		this.setRegistryName(FlintModInit.MODID, name);
	}
}
