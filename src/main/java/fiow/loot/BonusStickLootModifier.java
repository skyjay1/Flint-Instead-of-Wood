package fiow.loot;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;

public class BonusStickLootModifier extends LootModifier {

    private final TagKey<Block> leavesTag;
    private final float stickChance;
    private final RegistryObject<Item> stick;
    private final int count;

    protected BonusStickLootModifier(final LootItemCondition[] conditionsIn, final TagKey<Block> leavesTag,
                                     final float stickChance, final RegistryObject<Item> stick, final int count) {
        super(conditionsIn);
        this.leavesTag = leavesTag;
        this.stickChance = stickChance;
        this.stick = stick;
        this.count = count;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        // do not apply when incorrectly parsed
        if(!stick.isPresent()) {
            return generatedLoot;
        }
        // determine loot parameter values
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        BlockState block = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        // do not apply when missing a value
        if (entity == null || tool == null || block == null) {
            return generatedLoot;
        }
        // do not apply when using shears or breaking other blocks
        if (tool.getItem() == Items.SHEARS || !block.is(leavesTag)) {
            return generatedLoot;
        }
        // do not apply when using silk touch tool
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
            return generatedLoot;
        }
        // determine if loot bonus should apply
        if (entity.level.getRandom().nextFloat() > this.stickChance) {
            return generatedLoot;
        }
        // add stick to loot
        generatedLoot.add(new ItemStack(stick.get(), count));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<BonusStickLootModifier> {

        private static final String LEAVES = "leaves";
        private static final String CHANCE = "chance";
        private static final String STICK = "stick";
        private static final String COUNT = "count";

        @Override
        public BonusStickLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            ResourceLocation leavesId = new ResourceLocation(GsonHelper.getAsString(object, LEAVES, BlockTags.LEAVES.location().toString()));
            TagKey<Block> leaves = ForgeRegistries.BLOCKS.tags().createTagKey(leavesId);
            float chance = GsonHelper.getAsFloat(object, CHANCE, 0.5F);
            ResourceLocation stickId = new ResourceLocation(GsonHelper.getAsString(object, STICK, Items.STICK.getRegistryName().toString()));
            RegistryObject<Item> stick = RegistryObject.create(stickId, ForgeRegistries.ITEMS);
            int count = GsonHelper.getAsInt(object, COUNT, 1);
            return new BonusStickLootModifier(conditionsIn, leaves, chance, stick, count);
        }

        @Override
        public JsonObject write(BonusStickLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add(LEAVES, new JsonPrimitive(instance.leavesTag.location().toString()));
            json.add(CHANCE, new JsonPrimitive(instance.stickChance));
            json.add(STICK, new JsonPrimitive(instance.stick.getId().toString()));
            json.add(COUNT, new JsonPrimitive(instance.count));
            return json;
        }
    }
}
