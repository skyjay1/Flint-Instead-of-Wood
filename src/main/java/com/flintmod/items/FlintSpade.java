package com.flintmod.items;

import java.util.List;

import javax.annotation.Nullable;

import com.flintmod.main.FlintModInit;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FlintSpade extends ItemSpade
{
	public FlintSpade(String name) 
	{
		super(FlintItems.FLINT);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}
	
	/**
     * allows items to add custom lines of information to the mouseover description
     */
	@Override
	@SideOnly(Side.CLIENT)
	 public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn)
	 {
		info.add(I18n.format("tooltip.increased_chance_to_harvest"));
		info.add(I18n.format("tooltip.flint_from_gravel"));
	}
}
