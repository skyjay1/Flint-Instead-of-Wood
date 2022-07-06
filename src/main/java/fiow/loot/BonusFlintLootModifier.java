package fiow.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fiow.Fiow;
import fiow.FiowRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

    private final float flintChance;
    private final RegistryObject<Item> flint;
    private final int count;

    protected BonusFlintLootModifier(final LootItemCondition[] conditionsIn, final float flintChance,
                                     final RegistryObject<Item> flint, final int count) {
        super(conditionsIn);
        this.flintChance = flintChance;
        this.flint = flint;
        this.count = count;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // do not apply when incorrectly parsed or loot is empty
        if(!flint.isPresent() || generatedLoot.isEmpty()) {
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
        // do not apply when using silk touch tool
        if (tool.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
            return generatedLoot;
        }
        // replace loot with flint
        return ObjectArrayList.of(new ItemStack(flint.get(), count));
    }

    public static class Serializer extends GlobalLootModifierSerializer<BonusFlintLootModifier> {

        private static final String CHANCE = "chance";
        private static final String FLINT = "flint";
        private static final String COUNT = "count";

        @Override
        public BonusFlintLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            float chance = GsonHelper.getAsFloat(object, CHANCE, 0.5F);
            ResourceLocation flintId = new ResourceLocation(GsonHelper.getAsString(object, FLINT, ForgeRegistries.ITEMS.getKey(Items.FLINT).toString()));
            RegistryObject<Item> flint = RegistryObject.create(flintId, ForgeRegistries.ITEMS);
            int count = GsonHelper.getAsInt(object, COUNT, 1);
            return new BonusFlintLootModifier(conditionsIn, chance, flint, count);
        }

        @Override
        public JsonObject write(BonusFlintLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add(CHANCE, new JsonPrimitive(instance.flintChance));
            json.add(FLINT, new JsonPrimitive(instance.flint.getId().toString()));
            json.add(COUNT, new JsonPrimitive(instance.count));
            return json;
        }
    }
}
