package fiow.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.util.UUID;

public class KnifeItem extends SwordItem {

    protected static final UUID BASE_ATTACK_RANGE_UUID = UUID.fromString("729edc32-6169-40d8-904a-3be6f6546333");
    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final float attackSpeed;
    private final float attackRange;

    /**
     * Constructor for KnifeItem. The actual logic to prevent attacks is found in
     * {@link fiow.FiowEvents.ForgeHandler#onAttackTarget(AttackEntityEvent)}
     *
     * @param iItemTier        the item tier, used for durability, etc.
     * @param baseAttackDamage the attack damage modifier
     * @param attackSpeed      the attack speed modifier
     * @param attackRange      the attack range modifier (should be negative)
     * @param properties       the item properties
     */
    public KnifeItem(IItemTier iItemTier, int baseAttackDamage, float attackSpeed, float attackRange, Properties properties) {
        super(iItemTier, baseAttackDamage, attackSpeed, properties);
        this.attackSpeed = attackSpeed;
        this.attackRange = Math.min(0, attackRange);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slot) {
        // lazy-load modifier map (because forge attributes are not available sooner)
        if (null == this.defaultModifiers) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage(), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Weapon modifier", attackRange, AttributeModifier.Operation.ADDITION));
            this.defaultModifiers = builder.build();
        }
        return slot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
