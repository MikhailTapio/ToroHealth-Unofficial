package net.torocraft.torohealth.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderBuffers;
import net.torocraft.torohealth.api.IGuiGraphics;
import net.torocraft.torohealth.api.IGuiGraphicsGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IGuiGraphicsGetter {


    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    private GuiGraphics getGuiGraphicsRaw() {
        return new GuiGraphics((Minecraft) (Object) this, renderBuffers.bufferSource());
    }

    @Override
    public GuiGraphics getGuiGraphics(PoseStack pose) {
        final GuiGraphics graphics = getGuiGraphicsRaw();
        ((IGuiGraphics) graphics).setPose(pose);
        return graphics;
    }
}
