package com.flintmod.main;

import com.flintmod.items.FlintItemInit;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
		World world = event.getEntityPlayer().worldObj;
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
						final float amount = 0.1428571428571F; // 1 health / 7 hits per log with no tool
						player.attackEntityFrom(DamageSource.generic, amount);
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
				event.getWorld().spawnEntityInWorld(entityitem);
			}
		}
	}
	
	@SubscribeEvent
	public void onHarvestBlock(BlockEvent.HarvestDropsEvent event)
	{
		final int FLINT_CHANCE = 50;
		final EntityPlayer PLAYER = event.getHarvester();
		// null-check before accessing ItemStack (fix #2)
		if(null == PLAYER || null == PLAYER.getHeldItemMainhand()) return;

		if(event.getState().getBlock() == Blocks.GRAVEL && event.getWorld().rand.nextInt(100) < FLINT_CHANCE)
		{
			if(PLAYER.getHeldItemMainhand().getItem() == FlintItemInit.flintShovel)
			{
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Items.FLINT, 1, 0));
			}
		}
	}
	
	public static boolean isAxe(ItemStack s)
	{
		return s != null && s.getItem() != null && s.getItem().getHarvestLevel(s, "axe") >= 0;
	}
}
