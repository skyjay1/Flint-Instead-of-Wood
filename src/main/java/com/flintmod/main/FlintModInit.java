package com.flintmod.main;

import com.flintmod.items.FlintItemInit;
import com.flintmod.proxies.CommonProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=FlintModInit.MODID, name=FlintModInit.MOD_NAME, version=FlintModInit.MOD_VERSION, acceptedMinecraftVersions=FlintModInit.MINECRAFT_VERSION)
public class FlintModInit
{
	public static final String MODID = "flintmod";
	public static final String MOD_NAME = "Flint Instead of Wood";
	public static final String MOD_VERSION = "8.1";
	public static final String MINECRAFT_VERSION = "1.11.2";
		
	@SidedProxy(clientSide="com." + MODID + ".proxies.ClientProxy", serverSide="com." + MODID + ".proxies.CommonProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance(MODID)
	public static FlintModInit instance;

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		Config.mainRegistry(new Configuration(event.getSuggestedConfigurationFile()));
		FlintItemInit.mainRegistry();
		CraftingHandler.mainRegistry();
		proxy.registerRenders();
	}

	@Mod.EventHandler
	public static void init(FMLInitializationEvent event) 
	{
		MinecraftForge.EVENT_BUS.register(new FlintEventHandler());
	}
}