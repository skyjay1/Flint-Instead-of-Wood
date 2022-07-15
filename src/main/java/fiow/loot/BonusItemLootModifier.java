package fiow.loot;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class BonusItemLootModifier extends LootModifier {

    public static final Codec<ItemStack> ITEM_OR_STACK_CODEC = Codec.either(ForgeRegistries.ITEMS.getCodec(), ItemStack.CODEC)
            .xmap(either -> either.map(ItemStack::new, Function.identity()),
                    stack -> stack.getCount() == 1 && !stack.hasTag()
                            ? Either.left(stack.getItem())
                            : Either.right(stack));

    public static final Supplier<Codec<BonusItemLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst ->
            codecStart(inst).and(ITEM_OR_STACK_CODEC.fieldOf("bonus_item").forGetter(BonusItemLootModifier::getBonusItem))
                    .and(TagKey.codec(Registry.BLOCK_REGISTRY).optionalFieldOf("block_tag").forGetter(BonusItemLootModifier::getBlockTag))
                    .and(Codec.BOOL.optionalFieldOf("replace", false).forGetter(BonusItemLootModifier::isReplace))
                    .apply(inst, BonusItemLootModifier::new)));

    private final ItemStack bonusItem;
    private final Optional<TagKey<Block>> blockTag;
    private final boolean replace;

    protected BonusItemLootModifier(final LootItemCondition[] conditionsIn, final ItemStack bonusItem,
                                       final Optional<TagKey<Block>> blockTag, final boolean replace) {
        super(conditionsIn);
        this.bonusItem = bonusItem;
        this.blockTag = blockTag;
        this.replace = replace;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // do not apply when incorrectly parsed or loot is empty
        if(bonusItem.isEmpty()) {
            return generatedLoot;
        }
        // determine loot parameter values
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        BlockState block = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        // do not apply when missing a parameter
        if (entity == null || tool == null || block == null) {
            return generatedLoot;
        }
        // do not apply when block does not match tag (if any)
        if(blockTag.isPresent() && !block.is(blockTag.get())) {
            return generatedLoot;
        }
        // replace loot with item
        return ObjectArrayList.of(bonusItem.copy());
    }

    public ItemStack getBonusItem() {
        return bonusItem;
    }

    public Optional<TagKey<Block>> getBlockTag() {
        return blockTag;
    }

    public boolean isReplace() {
        return replace;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
