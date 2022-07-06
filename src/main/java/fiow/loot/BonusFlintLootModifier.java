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
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;

public class BonusFlintLootModifier extends LootModifier {

    private final RegistryObject<Block> gravel;
    private final float flintChance;
    private final RegistryObject<Item> flint;
    private final int count;

    protected BonusFlintLootModifier(final LootItemCondition[] conditionsIn, final RegistryObject<Block> gravel,
                                     final float flintChance, final RegistryObject<Item> flint, final int count) {
        super(conditionsIn);
        this.gravel = gravel;
        this.flintChance = flintChance;
        this.flint = flint;
        this.count = count;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        // do not apply when incorrectly parsed or loot is empty
        if(!gravel.isPresent() || !flint.isPresent() || generatedLoot.isEmpty()) {
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
        // do not apply when using other tools or breaking other blocks
        if (tool.getItem() != FiowRegistry.FLINT_SHOVEL.get() || !block.is(gravel.get())) {
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
        return Lists.newArrayList(new ItemStack(flint.get(), count));
    }

    public static class Serializer extends GlobalLootModifierSerializer<BonusFlintLootModifier> {

        private static final String GRAVEL = "gravel";
        private static final String CHANCE = "chance";
        private static final String FLINT = "flint";
        private static final String COUNT = "count";

        @Override
        public BonusFlintLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            ResourceLocation gravelId = new ResourceLocation(GsonHelper.getAsString(object, GRAVEL, Blocks.GRAVEL.getRegistryName().toString()));
            RegistryObject<Block> gravel = RegistryObject.create(gravelId, ForgeRegistries.BLOCKS);
            float chance = GsonHelper.getAsFloat(object, CHANCE, 0.5F);
            ResourceLocation flintId = new ResourceLocation(GsonHelper.getAsString(object, FLINT, Items.FLINT.getRegistryName().toString()));
            RegistryObject<Item> flint = RegistryObject.create(flintId, ForgeRegistries.ITEMS);
            int count = GsonHelper.getAsInt(object, COUNT, 1);
            return new BonusFlintLootModifier(conditionsIn, gravel, chance, flint, count);
        }

        @Override
        public JsonObject write(BonusFlintLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add(GRAVEL, new JsonPrimitive(instance.gravel.getId().toString()));
            json.add(CHANCE, new JsonPrimitive(instance.flintChance));
            json.add(FLINT, new JsonPrimitive(instance.flint.getId().toString()));
            json.add(COUNT, new JsonPrimitive(instance.count));
            return json;
        }
    }
}
