package com.flintmod.items;

import java.util.List;

import com.flintmod.main.FlintModInit;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FlintSpade extends ItemSpade
{
	public FlintSpade(ToolMaterial m, String name) 
	{
		super(m);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
	}
	
	/**
     * allows items to add custom lines of information to the mouseover description
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean b) 
    {
    	info.add(I18n.format("tooltip.increased_chance_to_harvest"));
    	info.add(I18n.format("tooltip.flint_from_gravel"));
    }
}
