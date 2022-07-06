package fiow;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Fiow.MODID)
public final class Fiow {

    public static final String MODID = "fiow";

    public static final Logger LOGGER = LogManager.getFormatterLogger(MODID);

    private static final ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
    public static FiowConfig CONFIG = new FiowConfig(CONFIG_BUILDER);
    private static final ForgeConfigSpec CONFIG_SPEC = CONFIG_BUILDER.build();

    public Fiow() {
        // register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Fiow::loadConfig);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Fiow::reloadConfig);
        // registry events
        FiowRegistry.register();
        FMLJavaModLoadingContext.get().getModEventBus().register(FiowEvents.ModHandler.class);
        MinecraftForge.EVENT_BUS.register(FiowEvents.ForgeHandler.class);
        // setup event
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Fiow::setup);
    }

    public static void setup(final FMLCommonSetupEvent event) {

    }

    public static void loadConfig(final ModConfig.Loading event) {
        CONFIG.bake();
    }

    public static void reloadConfig(final ModConfig.Reloading event) {
        CONFIG.bake();
    }
}
