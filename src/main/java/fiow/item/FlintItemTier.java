package fiow.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

public class FlintItemTier implements IItemTier {

    public static final IItemTier FLINT = new FlintItemTier();

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
