package net.torocraft.torohealth;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.torocraft.torohealth.config.Config;
import net.torocraft.torohealth.config.loader.ConfigLoader;

import java.util.Random;

@Mod(ToroHealth.MODID)
public class ToroHealth {
    public static final String MODID = "torohealth";

    public static Config CONFIG = new Config();
    public static boolean IS_HOLDING_WEAPON = false;
    public static final Random RAND = new Random();

    private static final ConfigLoader<Config> CONFIG_LOADER = new ConfigLoader<>(new Config(), ToroHealth.MODID + ".json", config -> ToroHealth.CONFIG = config);

    public ToroHealth() {
        if (!FMLEnvironment.dist.isClient()) return;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        NeoForge.EVENT_BUS.register(this);
        ToroHealthClient.init();
    }

    private void setup(final FMLClientSetupEvent event) {
        CONFIG_LOADER.load();
    }

}
