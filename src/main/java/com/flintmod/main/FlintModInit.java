package com.flintmod.main;

import com.flintmod.items.FlintItemInit;
import com.flintmod.proxies.ClientProxy;
import com.flintmod.proxies.CommonProxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FlintModInit.MODID)
@Mod.EventBusSubscriber(modid = FlintModInit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlintModInit {
	
	public static final String MODID = "flintmod";
	public static final String MOD_NAME = "Flint Instead of Wood";
	public static final String MOD_VERSION = "8.1";
	public static final String MINECRAFT_VERSION = "1.11.2";

	public static final CommonProxy PROXY = DistExecutor.runForDist(() -> () -> new ClientProxy(),
			() -> () -> new CommonProxy());
	
	public FlintModInit() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(new FlintEventHandler());


		//GolemEntityTypes.init();
		//ExtraGolemsConfig.setupConfig();
		//ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER /*world*/, ExtraGolemsConfig.SERVER_CONFIG);
	}

	private void setup(final FMLCommonSetupEvent event) {
		
	}
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		PROXY.registerItems(event);
	}
	
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		PROXY.registerModels(event);
	}

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		//Config.mainRegistry(new Configuration(event.getSuggestedConfigurationFile()));
		FlintItemInit.mainRegistry();
		CraftingHandler.mainRegistry();
		PROXY.registerRenders();
	}
}