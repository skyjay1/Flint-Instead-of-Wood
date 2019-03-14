package com.flintmod.proxies;

import com.flintmod.items.FlintItemInit;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	
	@Override
public void registerModels(final ModelRegistryEvent event) {
		registerItem(FlintItemInit.FLINT_AXE);
		registerItem(FlintItemInit.FLINT_HOE);
		registerItem(FlintItemInit.FLINT_PICK);
		registerItem(FlintItemInit.FLINT_SHOVEL);
		registerItem(FlintItemInit.FLINT_SWORD);
		registerItem(FlintItemInit.FLINT_TOOL_HEAD);
	}

	private void registerItem(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
