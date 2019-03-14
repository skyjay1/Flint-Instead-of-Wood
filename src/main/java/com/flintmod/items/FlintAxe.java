package com.flintmod.items;

import com.flintmod.main.FlintModInit;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;

public class FlintAxe extends ItemAxe {
	// needed an exposed constructor because ItemAxe(ToolMaterial) is protected
	public FlintAxe(String name, final Item.Properties properties) {
		super(FlintItemInit.FLINT, 6.0F, -3.2F, properties.maxStackSize(1));
		this.setRegistryName(FlintModInit.MODID, name);
	}
}
