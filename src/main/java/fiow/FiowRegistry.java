package fiow;

import fiow.item.FlintItemTier;
import fiow.item.KnifeItem;
import fiow.loot.BonusFlintLootModifier;
import fiow.loot.BonusStickLootModifier;
import fiow.loot.RemoveWoodenToolsModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public final class FiowRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fiow.MODID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Fiow.MODID);

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOOT_MODIFIER_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        // setup listener
        FMLJavaModLoadingContext.get().getModEventBus().addListener(FiowRegistry::setup);
    }

    public static void setup(final FMLCommonSetupEvent event) {

    }

    //// ITEMS ////
    public static final RegistryObject<Item> FLINT_TOOL_HEAD = ITEMS.register("flint_tool_head", () ->
            new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> FLINT_AXE = ITEMS.register("flint_axe", () ->
            new AxeItem(FlintItemTier.FLINT, 6.0F, -3.2F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> FLINT_HOE = ITEMS.register("flint_hoe", () ->
            new HoeItem(FlintItemTier.FLINT, 0, -3.0F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> FLINT_KNIFE = ITEMS.register("flint_knife", () ->
            new KnifeItem(FlintItemTier.FLINT, 3, -1.7F, -1.0F, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> FLINT_SHOVEL = ITEMS.register("flint_shovel", () ->
            new ShovelItem(FlintItemTier.FLINT, 1.5F, -3.0F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)) {
                @Override
                public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> text, TooltipFlag flag) {
                    text.add(Component.translatable(itemStack.getItem().getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
                }
            });
    public static final RegistryObject<Item> FLINT_PICKAXE = ITEMS.register("flint_pickaxe", () ->
            new PickaxeItem(FlintItemTier.FLINT, 1, -2.8F, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

    //// LOOT MODIFIERS ////
    public static final RegistryObject<BonusFlintLootModifier.Serializer> BONUS_FLINT_LOOT_MODIFIER = LOOT_MODIFIER_SERIALIZERS.register(
            "bonus_flint", () -> new BonusFlintLootModifier.Serializer());
    public static final RegistryObject<BonusStickLootModifier.Serializer> BONUS_STICK_LOOT_MODIFIER = LOOT_MODIFIER_SERIALIZERS.register(
            "bonus_stick", () -> new BonusStickLootModifier.Serializer());
    public static final RegistryObject<RemoveWoodenToolsModifier.Serializer> REMOVE_WOODEN_TOOLS_MODIFIER = LOOT_MODIFIER_SERIALIZERS.register(
            "remove_wooden_tools", () -> new RemoveWoodenToolsModifier.Serializer());


}
