package fiow;

import fiow.item.FlintItemTier;
import fiow.item.KnifeItem;
import fiow.loot.BonusFlintLootModifier;
import fiow.loot.BonusStickLootModifier;
import fiow.loot.RemoveWoodenToolsModifier;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public final class FiowRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fiow.MODID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Fiow.MODID);

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
            new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> FLINT_AXE = ITEMS.register("flint_axe", () ->
            new AxeItem(FlintItemTier.FLINT, 6.0F, -3.2F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> FLINT_HOE = ITEMS.register("flint_hoe", () ->
            new HoeItem(FlintItemTier.FLINT, 0, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> FLINT_KNIFE = ITEMS.register("flint_knife", () ->
            new KnifeItem(FlintItemTier.FLINT, 3, -1.7F, -2.0F, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> FLINT_SHOVEL = ITEMS.register("flint_shovel", () ->
            new ShovelItem(FlintItemTier.FLINT, 1.5F, -3.0F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)) {
                @OnlyIn(Dist.CLIENT)
                public void appendHoverText(ItemStack itemStack, @Nullable World level, List<ITextComponent> text, ITooltipFlag flag) {
                    text.add(new TranslationTextComponent(itemStack.getItem().getDescriptionId() + ".tooltip").withStyle(TextFormatting.GRAY));
                }
            });
    public static final RegistryObject<Item> FLINT_PICKAXE = ITEMS.register("flint_pickaxe", () ->
            new PickaxeItem(FlintItemTier.FLINT, 1, -2.8F, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    //// LOOT MODIFIERS ////
    public static final RegistryObject<BonusFlintLootModifier.Serializer> BONUS_FLINT_LOOT_MODIFIER = LOOT_MODIFIER_SERIALIZERS.register(
            "bonus_flint", () -> new BonusFlintLootModifier.Serializer());
    public static final RegistryObject<BonusStickLootModifier.Serializer> BONUS_STICK_LOOT_MODIFIER = LOOT_MODIFIER_SERIALIZERS.register(
            "bonus_stick", () -> new BonusStickLootModifier.Serializer());
    public static final RegistryObject<RemoveWoodenToolsModifier.Serializer> REMOVE_WOODEN_TOOLS_MODIFIER = LOOT_MODIFIER_SERIALIZERS.register(
            "remove_wooden_tools", () -> new RemoveWoodenToolsModifier.Serializer());


}
