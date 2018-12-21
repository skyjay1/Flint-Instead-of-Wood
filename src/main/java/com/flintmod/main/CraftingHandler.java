package com.flintmod.main;

import java.util.Iterator;

import com.flintmod.items.FlintItemInit;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CraftingHandler 
{
	public static void mainRegistry()
	{
		if(Config.REMOVE_WOODEN)
		{
			removeRecipes(Items.WOODEN_AXE, Items.WOODEN_SHOVEL, Items.WOODEN_HOE, Items.WOODEN_PICKAXE, Items.WOODEN_SWORD);
		}
		if(Config.REMOVE_STONE)
		{
			removeRecipes(Items.STONE_AXE, Items.STONE_SHOVEL, Items.STONE_HOE, Items.STONE_PICKAXE, Items.STONE_SWORD);
		}

		addModRecipes();
	}

	private static void removeRecipes(Item... items)
	{
		Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator();
		while(iterator.hasNext())
		{
			ItemStack stack = ((IRecipe)iterator.next()).getRecipeOutput();
			if(stack != null && stack.getItem() != null)
			{
				Item item = stack.getItem();
				for(Item i : items)
				{
					if(item == i) iterator.remove();
				}
			}
		}
	}

	private static void addModRecipes()
	{
		GameRegistry.addShapedRecipe(new ItemStack(FlintItemInit.FLINT_TOOL_HEAD), "F "," F",'F',Items.FLINT);
		GameRegistry.addShapedRecipe(new ItemStack(FlintItemInit.FLINT_TOOL_HEAD), " F","F ",'F',Items.FLINT);
		
		if(Config.CRAFT_AXE)
		{
			GameRegistry.addShapedRecipe(new ItemStack(FlintItemInit.FLINT_AXE), "FF","SF",'H',FlintItemInit.FLINT_TOOL_HEAD,'F',Items.FLINT,'S',Items.STICK);
		}
		if(Config.CRAFT_HOE)
		{
			GameRegistry.addShapedRecipe(new ItemStack(FlintItemInit.FLINT_HOE), "FF","S ",'F',Items.FLINT,'S',Items.STICK);
		}
		if(Config.CRAFT_PICK)
		{
			GameRegistry.addShapedRecipe(new ItemStack(FlintItemInit.FLINT_PICK), "HF","S ",'H',FlintItemInit.FLINT_TOOL_HEAD,'F',Items.FLINT,'S',Items.STICK);
			GameRegistry.addShapedRecipe(new ItemStack(FlintItemInit.FLINT_PICK), "FH","S ",'H',FlintItemInit.FLINT_TOOL_HEAD,'F',Items.FLINT,'S',Items.STICK);
		}
		if(Config.CRAFT_SHOVEL)
		{
			GameRegistry.addShapedRecipe(new ItemStack(FlintItemInit.FLINT_SHOVEL), "F","S",'F',Items.FLINT,'S',Items.STICK);
		}
		if(Config.CRAFT_SWORD)
		{
			GameRegistry.addShapedRecipe(new ItemStack(FlintItemInit.FLINT_SWORD), "H","S",'H',FlintItemInit.FLINT_TOOL_HEAD,'S',Items.STICK);
		}
	}
}
