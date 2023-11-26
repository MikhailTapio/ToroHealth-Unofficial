package net.torocraft.torohealth.config;

import com.google.gson.annotations.JsonAdapter;
import net.torocraft.torohealth.config.loader.ColorJsonAdapter;
import net.torocraft.torohealth.config.loader.IConfig;

public class Config implements IConfig {
    public enum Mode {
        NONE, WHEN_HOLDING_WEAPON, ALWAYS
    }

    public enum NumberType {
        NONE, LAST, CUMULATIVE
    }

    public enum AnchorPoint {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }

    public final boolean watchForChanges = true;
    public final Hud hud = new Hud();
    public final Bar bar = new Bar();
    public final InWorld inWorld = new InWorld();
    public final Particle particle = new Particle();

    public static class Hud {
        public final int distance = 60;
        public final float x = 4f;
        public final float y = 4f;
        public final float scale = 1f;
        public final int hideDelay = 20;
        public final AnchorPoint anchorPoint = AnchorPoint.TOP_LEFT;
        public final boolean showEntity = true;
        public final boolean showBar = true;
        public final boolean showSkin = true;
        public final boolean onlyWhenHurt = false;
    }

    public static class Particle {
        public final boolean show = true;
        @JsonAdapter(ColorJsonAdapter.class)
        public final int damageColor = 0xff0000;
        @JsonAdapter(ColorJsonAdapter.class)
        public final int healColor = 0x00ff00;
        public final int distance = 60;
        public transient int distanceSquared = 0;
    }

    public static class Bar {
        public final NumberType damageNumberType = NumberType.LAST;
        @JsonAdapter(ColorJsonAdapter.class)
        public final int friendColor = 0x00ff00;
        @JsonAdapter(ColorJsonAdapter.class)
        public final int friendColorSecondary = 0x008000;
        @JsonAdapter(ColorJsonAdapter.class)
        public final int foeColor = 0xff0000;
        @JsonAdapter(ColorJsonAdapter.class)
        public final int foeColorSecondary = 0x800000;
    }

    public static class InWorld {
        public final Mode mode = Mode.NONE;
        public final float distance = 60f;
        public final boolean onlyWhenLookingAt = false;
        public final boolean onlyWhenHurt = false;
    }

    @Override
    public void update() {
        particle.distanceSquared = particle.distance * particle.distance;
    }

    @Override
    public boolean shouldWatch() {
        return watchForChanges;
    }

}
