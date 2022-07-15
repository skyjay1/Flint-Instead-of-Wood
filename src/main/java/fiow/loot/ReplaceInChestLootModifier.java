package fiow.loot;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fiow.Fiow;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ReplaceInChestLootModifier extends LootModifier {

    private static final Codec<ItemStack> ITEM_OR_STACK_CODEC = BonusItemLootModifier.ITEM_OR_STACK_CODEC;

    public static final Supplier<Codec<ReplaceInChestLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst ->
            codecStart(inst)
                    .and(Codec.STRING.listOf().fieldOf("paths").forGetter(ReplaceInChestLootModifier::getPaths))
                    .and(Codec.unboundedMap(ForgeRegistries.ITEMS.getCodec(), ITEM_OR_STACK_CODEC)
                            .fieldOf("replace").forGetter(ReplaceInChestLootModifier::getReplacementMap))
                    .apply(inst, ReplaceInChestLootModifier::new)));

    private final List<String> paths;
    private final Map<Item, ItemStack> itemMap;

    protected ReplaceInChestLootModifier(final LootItemCondition[] conditionsIn, final List<String> paths,
                                         final Map<Item, ItemStack> replacementMap) {
        super(conditionsIn);
        this.paths = ImmutableList.copyOf(paths);
        this.itemMap = ImmutableMap.copyOf(replacementMap);
    }

    protected List<String> getPaths() {
        return paths;
    }

    protected Map<Item, ItemStack> getReplacementMap() {
        return itemMap;
    }

    protected ItemStack getReplacement(final ItemStack original) {
        if(itemMap.containsKey(original.getItem())) {
            ItemStack result = itemMap.get(original.getItem()).copy();
            if(original.hasTag()) {
                result.setTag(result.getOrCreateTag().merge(original.getTag()));
            }
            return result;
        }
        return original;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!Fiow.CONFIG.replaceInLoot()) {
            return generatedLoot;
        }
        ResourceLocation lootTable = context.getQueriedLootTableId();
        boolean apply = false;
        // iterate over each path to check if this modifier can apply
        for(String path : paths) {
            if(lootTable.getPath().contains(path)) {
                apply = true;
                break;
            }
        }
        if (!apply) {
            return generatedLoot;
        }
        // iterate over loot and remove wooden tools
        ObjectArrayList<ItemStack> modifiedLoot = new ObjectArrayList<>();
        for (ItemStack itemStack : generatedLoot) {
            // replace items that are present in the map with their counterpart
            modifiedLoot.add(getReplacement(itemStack));
        }
        return modifiedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
