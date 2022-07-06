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

import javax.annotation.Nonnull;
import java.util.List;

public class BonusStickLootModifier extends LootModifier {

    private final TagKey<Block> leavesTag;
    private final float stickChance;
    private final Item stick;
    private final int count;

    protected BonusStickLootModifier(final LootItemCondition[] conditionsIn, final TagKey<Block> leavesTag,
                                     final float stickChance, final Item stick, final int count) {
        super(conditionsIn);
        this.leavesTag = leavesTag;
        this.stickChance = stickChance;
        this.stick = stick;
        this.count = count;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        BlockState block = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        // do not apply when missing a value or no loot
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
        generatedLoot.add(new ItemStack(stick, count));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<BonusStickLootModifier> {

        private static final String LEAVES = "leaves";
        private static final String CHANCE = "chance";
        private static final String STICK = "stick";
        private static final String COUNT = "count";

        @Override
        public BonusStickLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            String sLeavesFallback = BlockTags.LEAVES.location().toString();
            ResourceLocation sLeaves = new ResourceLocation(GsonHelper.getAsString(object, LEAVES, sLeavesFallback));
            TagKey<Block> leaves = ForgeRegistries.BLOCKS.tags().createTagKey(sLeaves);
            float chance = GsonHelper.getAsFloat(object, CHANCE, 0.5F);
            String sStickFallback = Items.STICK.getRegistryName().toString();
            ResourceLocation sFlint = new ResourceLocation(GsonHelper.getAsString(object, STICK, sStickFallback));
            Item stick = ForgeRegistries.ITEMS.getValue(sFlint);
            int count = GsonHelper.getAsInt(object, COUNT, 1);
            return new BonusStickLootModifier(conditionsIn, leaves, chance, stick, count);
        }

        @Override
        public JsonObject write(BonusStickLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add(LEAVES, new JsonPrimitive(instance.leavesTag.location().toString()));
            json.add(CHANCE, new JsonPrimitive(instance.stickChance));
            json.add(STICK, new JsonPrimitive(instance.stick.getRegistryName().toString()));
            json.add(COUNT, new JsonPrimitive(instance.count));
            return json;
        }
    }
}
