package com.flintmod.proxies;

import com.flintmod.items.FlintAxe;
import com.flintmod.items.FlintHoe;
import com.flintmod.items.FlintKnife;
import com.flintmod.items.FlintPickaxe;
import com.flintmod.items.FlintSpade;
import com.flintmod.main.FlintModInit;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;

public class CommonProxy {
		
	public void registerItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
			new Item(new Item.Properties().maxStackSize(64).group(ItemGroup.MATERIALS))
					.setRegistryName(FlintModInit.MODID, "tool_part_flint"),
			new FlintAxe("axe_flint", new Item.Properties().group(ItemGroup.TOOLS)),
			new FlintHoe("hoe_flint", new Item.Properties().group(ItemGroup.TOOLS)),
			new FlintPickaxe("pickaxe_flint", new Item.Properties().group(ItemGroup.TOOLS)),
			new FlintSpade("spade_flint", new Item.Properties().group(ItemGroup.TOOLS)),
			new FlintKnife("knife_flint", new Item.Properties().group(ItemGroup.COMBAT))
		); 
	}
	
	public void registerModels(final ModelRegistryEvent event) {
		
	}
}
