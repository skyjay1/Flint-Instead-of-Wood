package com.flintmod.main;

import com.flintmod.proxies.ClientProxy;
import com.flintmod.proxies.CommonProxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FlintModInit.MODID)
@Mod.EventBusSubscriber(modid = FlintModInit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlintModInit {
	
	public static final String MODID = "flintmod";

	public static final CommonProxy PROXY = DistExecutor.runForDist(() -> () -> new ClientProxy(),
			() -> () -> new CommonProxy());
	
	public FlintModInit() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FlintConfig.SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(new FlintEventHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		
	}
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		System.out.println("flintmod: RegisterItems");
		PROXY.registerItems(event);
	}
	
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		System.out.println("flintmod: RegisterModels");
		PROXY.registerModels(event);
	}
	
	@SubscribeEvent
	public static void onLoadConfig(final ModConfig.Loading configEvent) {
		
	}
//
//	@Mod.EventHandler
//	public static void preInit(FMLPreInitializationEvent event) {
//		//Config.mainRegistry(new Configuration(event.getSuggestedConfigurationFile()));
//		FlintItemInit.mainRegistry();
//		CraftingHandler.mainRegistry();
//		PROXY.registerRenders();
//	}
}