package com.flintmod.proxies;

import com.flintmod.items.FlintItems;
import com.flintmod.main.FlintModInit;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = FlintModInit.MODID)
public class ClientProxy extends CommonProxy 
{
	
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		FlintModInit.proxy.registerRenders();;
	}
	
	@Override
	public void registerRenders() 
	{
		registerItem(FlintItems.FLINT_TOOL_HEAD);
		registerItem(FlintItems.FLINT_AXE);
		registerItem(FlintItems.FLINT_HOE);
		registerItem(FlintItems.FLINT_PICK);
		registerItem(FlintItems.FLINT_SHOVEL);
		registerItem(FlintItems.FLINT_SWORD);
	}
	
	private void registerItem(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
