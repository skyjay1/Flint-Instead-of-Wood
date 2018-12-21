package com.flintmod.items;

import com.flintmod.main.FlintModInit;
import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;

public class FlintKnife extends ItemSword
{

	public FlintKnife(ToolMaterial material, String name) 
	{
		super(material);
		this.setUnlocalizedName(name);
		this.setRegistryName(FlintModInit.MODID, name);
	}
	
	 /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
	@Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
        	String key = SharedMonsterAttributes.ATTACK_SPEED.getName();
            multimap.removeAll(key);
            multimap.put(key, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.1000000953674316D, 0));
        }

        return multimap;
    }

}
