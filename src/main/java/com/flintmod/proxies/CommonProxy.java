package com.flintmod.proxies;

import com.flintmod.items.*;
import com.flintmod.main.EmptyRecipe;
import com.flintmod.main.FlintEventHandler;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;

@Mod.EventBusSubscriber
public class CommonProxy 
{
	public void registerRenders() {}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				new Item().setUnlocalizedName("tool_part_flint").setRegistryName("tool_part_flint"),
				new FlintKnife("knife_flint"),
				new FlintHoe("hoe_flint"),
				new FlintSpade("spade_flint"),
				new FlintAxe("axe_flint"),
				new FlintPickaxe("pickaxe_flint"));
	}
}
