package fiow.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class FlintItemTier implements Tier {

    public static final Tier FLINT = new FlintItemTier();

    @Override
    public int getUses() {
        return 59;
    }

    @Override
    public float getSpeed() {
        return 2.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(new ItemStack(Items.FLINT));
    }
}
