package com.flintmod.proxies;

import com.flintmod.items.FlintAxe;
import com.flintmod.items.FlintHoe;
import com.flintmod.items.FlintKnife;
import com.flintmod.items.FlintPickaxe;
import com.flintmod.items.FlintSpade;
import com.flintmod.main.FlintConfig;
import com.flintmod.main.FlintModInit;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;

public class CommonProxy {
		
	public void registerItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(
			new Item(new Item.Properties().maxStackSize(64).group(ItemGroup.MATERIALS))
				.setRegistryName(FlintModInit.MODID, "tool_part_flint"));
		
		//if(FlintConfig.ITEMS.ENABLE_AXE.get()) {
			event.getRegistry().register(new FlintAxe("axe_flint", new Item.Properties().group(ItemGroup.TOOLS)));
		//}
		//if(FlintConfig.ITEMS.ENABLE_HOE.get()) {
			event.getRegistry().register(new FlintHoe("hoe_flint", new Item.Properties().group(ItemGroup.TOOLS)));
		//}
		//if(FlintConfig.ITEMS.ENABLE_PICK.get()) {
			event.getRegistry().register(new FlintPickaxe("pickaxe_flint", new Item.Properties().group(ItemGroup.TOOLS)));
		//}
		//if(FlintConfig.ITEMS.ENABLE_SHOVEL.get()) {
			event.getRegistry().register(new FlintSpade("spade_flint", new Item.Properties().group(ItemGroup.TOOLS)));
		//}
		//if(FlintConfig.ITEMS.ENABLE_SWORD.get()) {
			event.getRegistry().register(new FlintKnife("knife_flint", new Item.Properties().group(ItemGroup.COMBAT)));
		//} 
	}
	
	public void registerModels(final ModelRegistryEvent event) {
		
	}
}
