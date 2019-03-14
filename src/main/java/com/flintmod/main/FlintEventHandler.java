package com.flintmod.main;

import com.flintmod.items.FlintItemInit;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FlintEventHandler {
	private static final Item[] WOOD_TOOLS = { Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_PICKAXE,
			Items.WOODEN_SHOVEL, Items.WOODEN_SWORD };
	private static final Item[] STONE_TOOLS = { Items.STONE_AXE, Items.STONE_HOE, Items.STONE_PICKAXE,
			Items.STONE_SHOVEL, Items.STONE_SWORD };
	private static final Item[] FLINT_TOOLS = { FlintItemInit.FLINT_AXE, FlintItemInit.FLINT_HOE,
			FlintItemInit.FLINT_PICK, FlintItemInit.FLINT_SHOVEL, FlintItemInit.FLINT_SWORD };

	/** Used to replace wooden tools in loot tables with flint ones **/
	@SubscribeEvent
	public void onLoadLootTable(LootTableLoadEvent event) {
		// first check if config allows you to do this
		final boolean replaceWood = FlintConfig.WORLD.REPLACE_IN_LOOT_CHESTS_WOODEN.get();
		final boolean replaceStone = FlintConfig.WORLD.REPLACE_IN_LOOT_CHESTS_STONE.get();
		if (!(replaceWood || replaceStone))
			return;
		// check for pools until there aren't any left to check
		String poolName = "";
		int poolsFound = 0;
		while (true) {
			poolName = getNextPoolName(poolsFound);
			LootPool pool = event.getTable().getPool(poolName);

			if (null == pool) {
				// there's no pool with this name, so give up and finish
				return;
			}

			// go through each tool and try to replace the wooden version with flint
			for (int i = 0, l = WOOD_TOOLS.length; i < l; i++) {
				Item woodTool = WOOD_TOOLS[i];
				String name = woodTool.getRegistryName().toString();
				LootEntry oldEntry = pool.getEntry(name);
				if (oldEntry != null) {
					Item replacement = FLINT_TOOLS[i];
					int weight = 3;
					int quality = 1;
					LootEntry newEntry = new LootEntryItem(replacement, weight, quality, new LootFunction[] {},
							new LootCondition[] {}, replacement.getRegistryName().toString());
					pool.removeEntry(name);
					pool.addEntry(newEntry);
				}
			}
			// increase this field to check for a different pool next time
			++poolsFound;
		}

	}

	/**
	 * Prevents breaking wood without a tool, if enabled. Hurts the player
	 * otherwise.
	 **/
	@SubscribeEvent
	public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getEntityPlayer().getEntityWorld();
		ItemStack stack = player.getHeldItemMainhand();
		IBlockState state = event.getState();

		if (state.getBlock() instanceof BlockLog) {
			if (!isAxe(stack)) {
				if (FlintConfig.DISABLE_BREAKING_WOOD) {
					event.setCanceled(true);
				} else if (FlintConfig.HURT_WHEN_BREAKING_WOOD) {
					if (stack == null
							|| !(stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword)) {
						float amount = 0.1428571428571F; // 1 health / 7 hits per log with no tool
						if (player.getMaxHealth() - player.getHealth() < 0.25F) {
							amount *= 4; // if they're at full health, get their attention with extra damage
						}
						player.attackEntityFrom(DamageSource.GENERIC, amount);
					}
				}
			}
		}
	}

	/** Used to make sticks drop from leaves **/
	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event) {
		if (event.getState().getBlock() instanceof BlockLeaves && event.getWorld() instanceof World
				&& event.getWorld().getRandom().nextInt(100) < FlintConfig.WORLD.CHANCE_DROP_STICKS.get()) {
			BlockPos pos = event.getPos();
			EntityItem entityitem = new EntityItem((World)event.getWorld(), pos.getX(), pos.getY(), pos.getZ(),
					new ItemStack(Items.STICK, 1));
			float f3 = 0.02F;
			entityitem.motionX = (double) ((float) event.getWorld().getRandom().nextGaussian() * f3);
			entityitem.motionY = (double) ((float) event.getWorld().getRandom().nextGaussian() * f3 + 0.2F);
			entityitem.motionZ = (double) ((float) event.getWorld().getRandom().nextGaussian() * f3);
			event.getWorld().spawnEntity(entityitem);
		}
	}

	/** Used to make flint shovel harvest flint from gravel more effectively **/
	@SubscribeEvent
	public void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
		final EntityPlayer PLAYER = event.getHarvester();
		final ItemStack HELD = PLAYER != null ? PLAYER.getHeldItemMainhand() : null;
		// much more likely to get flint from gravel when using flint shovel
		if (HELD != null && HELD.getItem() == FlintItemInit.FLINT_SHOVEL) {
			int looting = Math.max(0, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, HELD));
			final int FLINT_CHANCE = 50 + (looting * 15);

			if (event.getState().getBlock() == Blocks.GRAVEL && event.getWorld().getRandom().nextInt(100) < FLINT_CHANCE) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Items.FLINT, 1));
			}
		}
	}

	/** Shortcut method to detect if a given ItemStack is considered an Axe **/
	public static boolean isAxe(final EntityPlayer player, final IBlockState state, final ItemStack s) {
		return s != null && s.getItem() != null
				&& (s.getItem() instanceof ItemAxe || s.getHarvestLevel(ToolType.AXE, player, state) >= 0);
	}

	private static String getNextPoolName(int poolsFound) {
		return poolsFound <= 0 ? "main" : "pool" + poolsFound;
	}
}
