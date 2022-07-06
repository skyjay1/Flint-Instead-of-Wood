package fiow.loot;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fiow.Fiow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoveWoodenToolsModifier extends LootModifier {

    private final Map<Item, Item> itemMap;

    protected RemoveWoodenToolsModifier(final LootItemCondition[] conditionsIn, Map<Item, Item> itemMap) {
        super(conditionsIn);
        this.itemMap = itemMap;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (!Fiow.CONFIG.replaceInLoot()) {
            return generatedLoot;
        }
        ResourceLocation lootTable = context.getQueriedLootTableId();
        if (!(lootTable.getPath().contains("chests/") || lootTable.getPath().contains("gameplay/"))) {
            return generatedLoot;
        }
        // iterate over loot and remove wooden tools
        List<ItemStack> modifiedLoot = new ArrayList<>();
        for (ItemStack itemStack : generatedLoot) {
            // replace items that are present in the map with their counterpart
            Item replace = itemMap.get(itemStack.getItem());
            if (replace != null) {
                modifiedLoot.add(new ItemStack(replace, itemStack.getCount(), itemStack.getTag()));
            } else {
                modifiedLoot.add(itemStack);
            }
        }
        return modifiedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<RemoveWoodenToolsModifier> {

        private static final String AXE = Items.WOODEN_AXE.getRegistryName().toString();
        private static final String HOE = Items.WOODEN_HOE.getRegistryName().toString();
        private static final String PICKAXE = Items.WOODEN_PICKAXE.getRegistryName().toString();
        private static final String SHOVEL = Items.WOODEN_SHOVEL.getRegistryName().toString();
        private static final String SWORD = Items.WOODEN_SWORD.getRegistryName().toString();

        @Override
        public RemoveWoodenToolsModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            // locate resource location for each item
            ResourceLocation sAxe = new ResourceLocation(GsonHelper.getAsString(object, AXE, AXE));
            ResourceLocation sHoe = new ResourceLocation(GsonHelper.getAsString(object, HOE, HOE));
            ResourceLocation sPickaxe = new ResourceLocation(GsonHelper.getAsString(object, PICKAXE, PICKAXE));
            ResourceLocation sShovel = new ResourceLocation(GsonHelper.getAsString(object, SHOVEL, SHOVEL));
            ResourceLocation sSword = new ResourceLocation(GsonHelper.getAsString(object, SWORD, SWORD));

            // build map for the given items
            ImmutableMap<Item, Item> map = new ImmutableMap
                    .Builder<Item, Item>()
                    .put(Items.WOODEN_AXE, ForgeRegistries.ITEMS.getValue(sAxe))
                    .put(Items.WOODEN_HOE, ForgeRegistries.ITEMS.getValue(sHoe))
                    .put(Items.WOODEN_PICKAXE, ForgeRegistries.ITEMS.getValue(sPickaxe))
                    .put(Items.WOODEN_SHOVEL, ForgeRegistries.ITEMS.getValue(sShovel))
                    .put(Items.WOODEN_SWORD, ForgeRegistries.ITEMS.getValue(sSword))
                    .build();

            return new RemoveWoodenToolsModifier(conditionsIn, map);
        }

        @Override
        public JsonObject write(RemoveWoodenToolsModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.add(AXE, new JsonPrimitive(instance.itemMap.getOrDefault(Items.WOODEN_AXE, Items.AIR).getRegistryName().toString()));
            json.add(HOE, new JsonPrimitive(instance.itemMap.getOrDefault(Items.WOODEN_HOE, Items.AIR).getRegistryName().toString()));
            json.add(PICKAXE, new JsonPrimitive(instance.itemMap.getOrDefault(Items.WOODEN_PICKAXE, Items.AIR).getRegistryName().toString()));
            json.add(SHOVEL, new JsonPrimitive(instance.itemMap.getOrDefault(Items.WOODEN_SHOVEL, Items.AIR).getRegistryName().toString()));
            json.add(SWORD, new JsonPrimitive(instance.itemMap.getOrDefault(Items.WOODEN_SWORD, Items.AIR).getRegistryName().toString()));
            return json;
        }
    }
}
