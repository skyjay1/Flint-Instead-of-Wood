package fiow.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

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
    public KnifeItem(Tier iItemTier, int baseAttackDamage, float attackSpeed, float attackRange, Properties properties) {
        super(iItemTier, baseAttackDamage, attackSpeed, properties);
        this.attackSpeed = attackSpeed;
        this.attackRange = Math.min(0, attackRange);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        // lazy-load modifier map (because forge attributes are not available sooner)
        if (null == this.defaultModifiers) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage(), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
            builder.put(ForgeMod.ATTACK_RANGE.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Weapon modifier", attackRange, AttributeModifier.Operation.ADDITION));
            this.defaultModifiers = builder.build();
        }
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
