package com.flintmod.main;

import com.flintmod.items.FlintItemInit;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FlintEventHandler 
{
	@SubscribeEvent
	public void onBreakSpeed(PlayerEvent.BreakSpeed event)
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
					if(stack == null || !(stack.getItem() instanceof ItemTool))
					{
						float amount = 0.1428571428571F; // 1 health / 7 hits per log with no tool
						if(player.getMaxHealth() - player.getHealth() < 0.25F)
						{
							amount *= 4; // if they're at full health, get their attention with extra damage
						}
						player.attackEntityFrom(DamageSource.GENERIC, amount);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event)
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
	
	@SubscribeEvent
	public void onHarvestBlock(BlockEvent.HarvestDropsEvent event)
	{
		final EntityPlayer PLAYER = event.getHarvester();
		final ItemStack HELD = PLAYER != null ? PLAYER.getHeldItemMainhand() : null;
		// much more likely to get flint from gravel when using flint shovel
		if(HELD != null && HELD.getItem() == FlintItemInit.flintShovel)
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
	
	public static boolean isAxe(ItemStack s)
	{
		return s != null && s.getItem() != null && (s.getItem() instanceof ItemAxe || s.getItem().getHarvestLevel(s, "axe", null, null) >= 0);
	}
}
