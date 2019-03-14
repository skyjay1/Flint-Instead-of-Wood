package com.flintmod.items;

import com.flintmod.main.FlintConfig;
import com.flintmod.main.FlintModInit;

import net.minecraft.init.Items;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.ObjectHolder;

public class FlintItemInit {
	
	public static final IItemTier FLINT = new IItemTier() {
		public int getMaxUses() { return FlintConfig.ITEMS.FLINT_DURABILITY.get(); }

		public float getEfficiency() { return 2.0F; }

		public float getAttackDamage() { return FlintConfig.ITEMS.FLINT_TIER.get(); }

		public int getHarvestLevel() { return FlintConfig.ITEMS.FLINT_TIER.get(); }

		public int getEnchantability() { return 15; }

		public Ingredient getRepairMaterial() { return Ingredient.fromStacks(new ItemStack(Items.FLINT)); }
	};

	// EnumHelper.addToolMaterial("FLINT", Config.FLINT_HARVEST_LEVEL,
	// Config.FLINT_DURABILITY, 2.0F, 0.0F, 15);

	@ObjectHolder(FlintModInit.MODID + ":tool_part_flint")
	public static Item FLINT_TOOL_HEAD;

	@ObjectHolder(FlintModInit.MODID + ":knife_flint")
	public static Item FLINT_SWORD;

	@ObjectHolder(FlintModInit.MODID + ":spade_flint")
	public static Item FLINT_SHOVEL;

	@ObjectHolder(FlintModInit.MODID + ":hoe_flint")
	public static Item FLINT_HOE;

	@ObjectHolder(FlintModInit.MODID + ":axe_flint")
	public static Item FLINT_AXE;

	@ObjectHolder(FlintModInit.MODID + ":pickaxe_flint")
	public static Item FLINT_PICK;
}
