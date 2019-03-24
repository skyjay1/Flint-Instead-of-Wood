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
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemTiered;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

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
		// TODO this method is not being called for loot chests, or at all for that matter
		
		
		// first check if config allows you to do this
		final boolean replaceWood = FlintConfig.WORLD.REPLACE_IN_LOOT_CHESTS_WOODEN.get();
		// check for pools until there aren't any left to check
		String poolName = "";
		int poolsFound = 0;
		while (poolsFound < 10) {
			poolName = getNextPoolName(poolsFound);
			LootPool pool = event.getTable().getPool(poolName);

			if (null == pool) {
				// there's no pool with this name, so give up and finish
				System.out.println("No pool found for pool #" + poolsFound);
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
					System.out.println("Replaced entry of " + name + " with " + newEntry.getEntryName());
				}
			}
			// increase this field to check for a different pool next time
			++poolsFound;
		}

	}
	
	@SubscribeEvent
	public void onCraftItem(net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent event) {
		if(isWooden(event.getCrafting()) && FlintConfig.WORLD.REMOVE_WOOD_RECIPES.get()) {
			event.getCrafting().setDamage(event.getCrafting().getMaxDamage());
			NBTTagCompound nbt = event.getCrafting().getOrCreateTag();
			nbt.setTag("AttributeModifiers", new NBTTagList());
		}
	}
	
	@SubscribeEvent
	public void onTickEvent(net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent event) {
		if(event.type == Type.PLAYER && event.side == LogicalSide.SERVER 
				&& event.player != null && event.player.ticksExisted % 4 == 0) {
			ItemStack mainhand = event.player.getHeldItemMainhand();
			ItemStack offhand = event.player.getHeldItemOffhand();
			if(isWooden(mainhand)) {
				mainhand.setDamage(mainhand.getMaxDamage());
				NBTTagCompound nbt = mainhand.getOrCreateTag();
				nbt.setTag("AttributeModifiers", new NBTTagList());
			}
			if(isWooden(offhand)) {
				offhand.setDamage(offhand.getMaxDamage());
				NBTTagCompound nbt = offhand.getOrCreateTag();
				nbt.setTag("AttributeModifiers", new NBTTagList());
			}
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onTooltip(net.minecraftforge.event.entity.player.ItemTooltipEvent event) {
//		if (isWooden(event.getItemStack())) {
//			net.minecraft.util.text.ITextComponent name = (new net.minecraft.util.text.TextComponentString(""))
//					 .appendSibling(event.getItemStack().getDisplayName())
//					 .applyTextStyle(event.getItemStack().getRarity().color);
//		      if (event.getItemStack().hasDisplayName()) {
//		         name.applyTextStyle(net.minecraft.util.text.TextFormatting.ITALIC);
//		      }
//			event.getToolTip().clear();
//			event.getToolTip().add(name);
//		}
	}
	
	@SubscribeEvent
	public void onAttackEntity(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
		if(isWooden(event.getEntityPlayer().getActiveItemStack())) {
			//event.setCanceled(true);
		}
	}

	/**
	 * Prevents breaking wood without a tool, if enabled. Hurts the player
	 * otherwise.
	 **/
	@SubscribeEvent
	public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		IBlockState state = event.getState();

		if (state.getBlock() instanceof BlockLog) {
			if (!isAxe(player, state, stack)) {
				if (FlintConfig.WORLD.DISABLE_BREAKING_WOOD.get()) {
					event.setCanceled(true);
				} else if (FlintConfig.WORLD.HURT_WHEN_BREAKING_WOOD.get()) {
					if (stack == null
							|| !(stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword)) {
						float amount = 0.2828571428571F; // 1 health / 7 hits per log with no tool
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
		if (/*FlintConfig.ITEMS.ENABLE_SHOVEL.get() &&*/ HELD != null && HELD.getItem() == FlintItemInit.FLINT_SHOVEL) {
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
				&& (s.getToolTypes().contains(ToolType.AXE) || s.getItem() instanceof ItemAxe);
	}
	
	/** @return true if the stack contains a non-null ItemTiered that is WOOD tier **/
	public static boolean isWooden(final ItemStack stack) {
		return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemTiered 
				&& ((ItemTiered)stack.getItem()).getTier() == ItemTier.WOOD;
	}

	private static String getNextPoolName(int poolsFound) {
		return poolsFound <= 0 ? "main" : "pool" + poolsFound;
	}
}
