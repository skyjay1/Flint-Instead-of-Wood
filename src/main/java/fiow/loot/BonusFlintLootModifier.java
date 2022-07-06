package fiow.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fiow.FiowRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class BonusFlintLootModifier extends LootModifier {

    private final Block gravel;
    private final float flintChance;
    private final Item flint;
    private final int count;

    protected BonusFlintLootModifier(final LootItemCondition[] conditionsIn, final Block gravel, final float flintChance, final Item flint, final int count) {
        super(conditionsIn);
        this.gravel = gravel;
        this.flintChance = flintChance;
        this.flint = flint;
        this.count = count;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        BlockState block = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        // do not apply when missing a value or no loot
        if (entity == null || tool == null || block == null || generatedLoot.isEmpty()) {
            return generatedLoot;
        }
        // do not apply when using other tools or breaking other blocks
        if (tool.getItem() != FiowRegistry.FLINT_SHOVEL.get() || !block.is(gravel)) {
            return generatedLoot;
        }
        // do not apply when using silk touch tool
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
            return generatedLoot;
        }
        // determine if loot bonus should apply
        float fortune = (float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
        if (entity.level.getRandom().nextFloat() > this.flintChance * (1.0F + fortune)) {
            return generatedLoot;
        }
        // replace items with flint
        return Lists.newArrayList(new ItemStack(flint, count));
    }

    public static class Serializer extends GlobalLootModifierSerializer<BonusFlintLootModifier> {

        private static final String GRAVEL = "gravel";
        private static final String CHANCE = "chance";
        private static final String FLINT = "flint";
        private static final String COUNT = "count";

        @Override
        public BonusFlintLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            String sGravelFallback = Blocks.GRAVEL.getRegistryName().toString();
            ResourceLocation sGravel = new ResourceLocation(GsonHelper.getAsString(object, GRAVEL, sGravelFallback));
            Block gravel = ForgeRegistries.BLOCKS.getValue(sGravel);
            float chance = GsonHelper.getAsFloat(object, CHANCE, 0.5F);
            String sFlintFallback = Items.FLINT.getRegistryName().toString();
            ResourceLocation sFlint = new ResourceLocation(GsonHelper.getAsString(object, FLINT, sFlintFallback));
            Item flint = ForgeRegistries.ITEMS.getValue(sFlint);
            int count = GsonHelper.getAsInt(object, COUNT, 1);
            return new BonusFlintLootModifier(conditionsIn, gravel, chance, flint, count);
        }

        @Override
        public JsonObject write(BonusFlintLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add(GRAVEL, new JsonPrimitive(instance.gravel.getRegistryName().toString()));
            json.add(CHANCE, new JsonPrimitive(instance.flintChance));
            json.add(FLINT, new JsonPrimitive(instance.flint.getRegistryName().toString()));
            json.add(COUNT, new JsonPrimitive(instance.count));
            return json;
        }
    }
}
