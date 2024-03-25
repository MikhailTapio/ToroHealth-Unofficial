package net.torocraft.torohealth.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.torocraft.torohealth.api.IGuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics implements IGuiGraphics {
    @Mutable
    @Shadow
    @Final
    private PoseStack pose;

    @Override
    public void toroHealth$setPose(PoseStack pose) {
        this.pose = pose;
    }
}
