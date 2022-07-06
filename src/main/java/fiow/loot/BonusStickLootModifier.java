package fiow.loot;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class BonusStickLootModifier extends LootModifier {

    private final ITag.INamedTag<Block> leavesTag;
    private final float stickChance;
    private final Item stick;
    private final int count;

    protected BonusStickLootModifier(final ILootCondition[] conditionsIn, final ITag.INamedTag<Block> leavesTag,
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
        Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootParameters.TOOL);
        BlockState block = context.getParamOrNull(LootParameters.BLOCK_STATE);
        // do not apply when missing a value or no loot
        if(entity == null || tool == null || block == null) {
            return generatedLoot;
        }
        // do not apply when using shears or breaking other blocks
        if(tool.getItem() == Items.SHEARS || !block.is(leavesTag)) {
            return generatedLoot;
        }
        // do not apply when using silk touch tool
        if(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
            return generatedLoot;
        }
        // determine if loot bonus should apply
        if(entity.level.getRandom().nextFloat() > this.stickChance) {
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
        public BonusStickLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            String sLeavesFallback = BlockTags.LEAVES.getName().toString();
            ResourceLocation sLeaves = new ResourceLocation(JSONUtils.getAsString(object, LEAVES, sLeavesFallback));
            ITag.INamedTag<Block> leaves = BlockTags.createOptional(sLeaves);
            float chance = JSONUtils.getAsFloat(object, CHANCE, 0.5F);
            String sStickFallback = Items.STICK.getRegistryName().toString();
            ResourceLocation sFlint = new ResourceLocation(JSONUtils.getAsString(object, STICK, sStickFallback));
            Item stick = ForgeRegistries.ITEMS.getValue(sFlint);
            int count = JSONUtils.getAsInt(object, COUNT, 1);
            return new BonusStickLootModifier(conditionsIn, leaves, chance, stick, count);
        }

        @Override
        public JsonObject write(BonusStickLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add(LEAVES, new JsonPrimitive(instance.leavesTag.getName().toString()));
            json.add(CHANCE, new JsonPrimitive(instance.stickChance));
            json.add(STICK, new JsonPrimitive(instance.stick.getRegistryName().toString()));
            json.add(COUNT, new JsonPrimitive(instance.count));
            return json;
        }
    }
}
