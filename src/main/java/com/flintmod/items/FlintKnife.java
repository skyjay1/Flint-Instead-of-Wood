package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class FlintKnife extends ItemSword {

	public FlintKnife(final String name, final Item.Properties properties) {
		super(FlintItemInit.FLINT, 3, -2.1F, properties.maxStackSize(1));
		this.setRegistryName(FlintModInit.MODID, name);
	}
}
