package com.flintmod.main;

import org.apache.logging.log4j.Level;

import com.flintmod.items.FlintItems;

import net.minecraft.block.BlockLeaves;
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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class FlintEventHandler 
{
	public static final Item[] WOOD_TOOLS = { Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_SWORD };
	public static final Item[] STONE_TOOLS = { Items.STONE_AXE, Items.STONE_HOE, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_SWORD };
	public static final Item[] FLINT_TOOLS = { FlintItems.FLINT_AXE, FlintItems.FLINT_HOE, FlintItems.FLINT_PICK, FlintItems.FLINT_SHOVEL, FlintItems.FLINT_SWORD };
	
	/* Used to remove default recipes for wooden tools
	@SubscribeEvent
	public void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) 
	{
		System.out.println("trying to remove recipes");
		
		IForgeRegistryModifiable modifiable = (IForgeRegistryModifiable) event.getRegistry();
		
		if(Config.REMOVE_WOODEN)
		{
			removeRecipes(modifiable, FlintEventHandler.WOOD_TOOLS);
		}
		if(Config.REMOVE_STONE)
		{
			removeRecipes(modifiable, FlintEventHandler.STONE_TOOLS);
		}
	}
	*/
		
	/** Used to replace wooden tools in loot tables with flint ones **/
	@SubscribeEvent
	public void onLoadLootTable(final LootTableLoadEvent event)
	{
		// first check if config allows you to do this
		if(!Config.REPLACE_IN_LOOT_CHESTS)
			return;
		// check for pools until there aren't any left to check
		String poolName = "";
		int poolsFound = 0;
		while(true)
		{
			poolName = getNextPoolName(poolsFound);
			LootPool pool = event.getTable().getPool(poolName);
			
			if(null == pool)
			{
				// there's no pool with this name, so give up and finish
				return;
			}
			
			// go through each tool and try to replace the wooden version with flint
			for(int i = 0, l = WOOD_TOOLS.length; i < l; i++)
			{
				Item woodTool = WOOD_TOOLS[i];
				String oldName = woodTool.getRegistryName().toString();
				LootEntry oldEntry = pool.getEntry(oldName);	
				if(oldEntry != null)
				{
					Item replacement = FLINT_TOOLS[i];
					String newName = replacement.getRegistryName().toString();
					int weight = 3;
					int quality = 1;
					LootEntry newEntry = new LootEntryItem(replacement, weight, quality, new LootFunction[] {}, new LootCondition[] {}, newName);
					pool.removeEntry(oldName);
					pool.addEntry(newEntry);
				}
			}
			// increase this field to check for a different pool next time
			++poolsFound;
		}
		
		
	}
	
	/** Prevents breaking wood without a tool, if enabled. Hurts the player otherwise. **/
	@SubscribeEvent
	public void onBreakSpeed(final PlayerEvent.BreakSpeed event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getEntityPlayer().getEntityWorld();
		ItemStack stack = player.getHeldItemMainhand();
		IBlockState state = event.getState();

		if(state.getBlock() == Blocks.LOG || state.getBlock() == Blocks.LOG2 || state.getBlock().isWood(world, event.getPos()))
		{
			if(!isAxe(stack)) 
			{
				if(Config.DISABLE_BREAKING_WOOD)
				{
					event.setCanceled(true);
				}
				else if(Config.HURT_WHEN_BREAKING_WOOD)
				{
					if(stack == null || !(stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword))
					{
						float amount = 0.1428571428571F; // 1 health / 7 hits per log with no tool
						if(player.getMaxHealth() - player.getHealth() < 0.25F)
						{
							amount *= 5; // if they're at full health, get their attention with extra damage
						}
						player.attackEntityFrom(DamageSource.GENERIC, amount);
					}
				}
			}
		}
	}

	/** Used to make sticks drop from leaves **/
	@SubscribeEvent
	public void onBreakBlock(final BlockEvent.BreakEvent event)
	{
		if(event.getState().getBlock() instanceof BlockLeaves)
		{
			if(event.getWorld().rand.nextInt(100) < Config.CHANCE_DROP_STICKS)
			{
				BlockPos pos = event.getPos();
				EntityItem entityitem = new EntityItem(event.getWorld(), pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.STICK, 1, 0));
				float f3 = 0.02F;
				entityitem.motionX = (double)((float)event.getWorld().rand.nextGaussian() * f3);
				entityitem.motionY = (double)((float)event.getWorld().rand.nextGaussian() * f3 + 0.2F);
				entityitem.motionZ = (double)((float)event.getWorld().rand.nextGaussian() * f3);
				event.getWorld().spawnEntity(entityitem);
			}
		}
	}
	
	/** Used to make flint shovel harvest flint from gravel more effectively **/
	@SubscribeEvent
	public void onHarvestBlock(final BlockEvent.HarvestDropsEvent event)
	{
		final EntityPlayer PLAYER = event.getHarvester();
		final ItemStack HELD = PLAYER != null ? PLAYER.getHeldItemMainhand() : null;
		// much more likely to get flint from gravel when using flint shovel
		if(HELD != null && HELD.getItem() == FlintItems.FLINT_SHOVEL)
		{		
			int looting = Math.max(0, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, HELD));
			final int FLINT_CHANCE = 50 + (looting * 15);

			if(event.getState().getBlock() == Blocks.GRAVEL && event.getWorld().rand.nextInt(100) < FLINT_CHANCE)
			{
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Items.FLINT, 1, 0));
			}
		}
	}
	
	/** Shortcut method to detect if a given ItemStack is considered an Axe **/
	public static boolean isAxe(final ItemStack s)
	{
		return s != null && s.getItem() != null && (s.getItem() instanceof ItemAxe || s.getItem().getHarvestLevel(s, "axe", null, null) >= 0);
	}
	
	private static String getNextPoolName(final int poolsFound)
	{
		return poolsFound <= 0 ? "main" : "pool" + poolsFound;
	}
	
	public static void removeDefaultRecipes()
	{
		IForgeRegistryModifiable<IRecipe> modifiable = (IForgeRegistryModifiable)ForgeRegistries.RECIPES;
		
		if(Config.REMOVE_WOODEN)
		{
			FlintModInit.LOGGER.log(Level.INFO, "Replacing Wood Tool Recipes. Disregard the following 'Potentially Dangerous alternative prefix' warnings for 'flintmod'.");
			removeRecipes(modifiable, FlintEventHandler.WOOD_TOOLS);
		}
		if(Config.REMOVE_STONE)
		{
			FlintModInit.LOGGER.log(Level.INFO, "Replacing Stone Tool Recipes. Disregard the following 'Potentially Dangerous alternative prefix' warnings for 'flintmod'.");
			removeRecipes(modifiable, FlintEventHandler.STONE_TOOLS);
		}
	}
	
	public static void removeRecipes(final IForgeRegistryModifiable<IRecipe> recipeRegistry, final Item... items)
	{		
		for(Item i : items)
		{
			ResourceLocation toRemove = i.getRegistryName();
			if(recipeRegistry.containsKey(toRemove))
			{
				IRecipe recipe = recipeRegistry.remove(toRemove);
				recipeRegistry.register(EmptyRecipe.from(recipe));
			}
		}
	}
}
