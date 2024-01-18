package net.torocraft.torohealth;

import net.neoforged.bus.api.IEventBus;
import net.torocraft.torohealth.display.Hud;

public class ToroHealthClient {
    public static final Hud HUD = new Hud();

    public static void init(IEventBus bus) {
        ClientEventHandler.init(bus);
    }

}
