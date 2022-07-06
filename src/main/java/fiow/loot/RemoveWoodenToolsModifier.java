package fiow.loot;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fiow.Fiow;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Map;

public class RemoveWoodenToolsModifier extends LootModifier {

    private final Map<ResourceLocation, RegistryObject<Item>> itemMap;

    protected RemoveWoodenToolsModifier(final LootItemCondition[] conditionsIn, Map<ResourceLocation, RegistryObject<Item>> itemMap) {
        super(conditionsIn);
        this.itemMap = itemMap;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!Fiow.CONFIG.replaceInLoot()) {
            return generatedLoot;
        }
        ResourceLocation lootTable = context.getQueriedLootTableId();
        if (!(lootTable.getPath().contains("chests/") || lootTable.getPath().contains("gameplay/"))) {
            return generatedLoot;
        }
        // iterate over loot and remove wooden tools
        ObjectArrayList<ItemStack> modifiedLoot = new ObjectArrayList<>();
        for (ItemStack itemStack : generatedLoot) {
            // replace items that are present in the map with their counterpart
            RegistryObject<Item> replace = itemMap.get(ForgeRegistries.ITEMS.getKey(itemStack.getItem()));
            if (replace != null && replace.isPresent()) {
                modifiedLoot.add(new ItemStack(replace.get(), itemStack.getCount(), itemStack.getTag()));
            } else {
                modifiedLoot.add(itemStack);
            }
        }
        return modifiedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<RemoveWoodenToolsModifier> {

        private static final String AXE = ForgeRegistries.ITEMS.getKey(Items.WOODEN_AXE).toString();
        private static final String HOE = ForgeRegistries.ITEMS.getKey(Items.WOODEN_HOE).toString();
        private static final String PICKAXE = ForgeRegistries.ITEMS.getKey(Items.WOODEN_PICKAXE).toString();
        private static final String SHOVEL = ForgeRegistries.ITEMS.getKey(Items.WOODEN_SHOVEL).toString();
        private static final String SWORD = ForgeRegistries.ITEMS.getKey(Items.WOODEN_SWORD).toString();

        @Override
        public RemoveWoodenToolsModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            // locate resource location for each item
            ResourceLocation axeId = new ResourceLocation(GsonHelper.getAsString(object, AXE, AXE));
            ResourceLocation hoeId = new ResourceLocation(GsonHelper.getAsString(object, HOE, HOE));
            ResourceLocation pickaxeId = new ResourceLocation(GsonHelper.getAsString(object, PICKAXE, PICKAXE));
            ResourceLocation shovelId = new ResourceLocation(GsonHelper.getAsString(object, SHOVEL, SHOVEL));
            ResourceLocation swordId = new ResourceLocation(GsonHelper.getAsString(object, SWORD, SWORD));

            // build map for the given items
            ImmutableMap<ResourceLocation, RegistryObject<Item>> map = new ImmutableMap
                    .Builder<ResourceLocation, RegistryObject<Item>>()
                    .put(ForgeRegistries.ITEMS.getKey(Items.WOODEN_AXE), RegistryObject.create(axeId, ForgeRegistries.ITEMS))
                    .put(ForgeRegistries.ITEMS.getKey(Items.WOODEN_HOE), RegistryObject.create(hoeId, ForgeRegistries.ITEMS))
                    .put(ForgeRegistries.ITEMS.getKey(Items.WOODEN_PICKAXE), RegistryObject.create(pickaxeId, ForgeRegistries.ITEMS))
                    .put(ForgeRegistries.ITEMS.getKey(Items.WOODEN_SHOVEL), RegistryObject.create(shovelId, ForgeRegistries.ITEMS))
                    .put(ForgeRegistries.ITEMS.getKey(Items.WOODEN_SWORD), RegistryObject.create(swordId, ForgeRegistries.ITEMS))
                    .build();

            return new RemoveWoodenToolsModifier(conditionsIn, map);
        }

        @Override
        public JsonObject write(RemoveWoodenToolsModifier instance) {
            RegistryObject<Item> AIR = RegistryObject.create(new ResourceLocation("air"), ForgeRegistries.ITEMS);
            JsonObject json = makeConditions(instance.conditions);
            json.add(AXE, new JsonPrimitive(instance.itemMap.getOrDefault(ForgeRegistries.ITEMS.getKey(Items.WOODEN_AXE), AIR).getId().toString()));
            json.add(HOE, new JsonPrimitive(instance.itemMap.getOrDefault(ForgeRegistries.ITEMS.getKey(Items.WOODEN_HOE), AIR).getId().toString()));
            json.add(PICKAXE, new JsonPrimitive(instance.itemMap.getOrDefault(ForgeRegistries.ITEMS.getKey(Items.WOODEN_PICKAXE), AIR).getId().toString()));
            json.add(SHOVEL, new JsonPrimitive(instance.itemMap.getOrDefault(ForgeRegistries.ITEMS.getKey(Items.WOODEN_SHOVEL), AIR).getId().toString()));
            json.add(SWORD, new JsonPrimitive(instance.itemMap.getOrDefault(ForgeRegistries.ITEMS.getKey(Items.WOODEN_SWORD), AIR).getId().toString()));
            return json;
        }
    }
}
