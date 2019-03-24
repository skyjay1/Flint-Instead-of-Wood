package com.flintmod.items;

import java.util.List;

import javax.annotation.Nullable;

import com.flintmod.main.FlintModInit;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlintSpade extends ItemSpade {
	public FlintSpade(final String name, final Item.Properties properties) {
		super(FlintItemInit.FLINT, 1.5F, -3, properties.maxStackSize(1));
		this.setRegistryName(FlintModInit.MODID, name);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		tooltip.add(new TextComponentTranslation("tooltip.increased_chance_to_harvest"));
		tooltip.add(new TextComponentTranslation("tooltip.flint_from_gravel", 
				"item.minecraft.flint", "block.minecraft.gravel"));
	}
}
