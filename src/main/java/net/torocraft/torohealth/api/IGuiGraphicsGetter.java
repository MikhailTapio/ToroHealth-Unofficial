package net.torocraft.torohealth.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public interface IGuiGraphicsGetter {
    GuiGraphics toroHealth$getGuiGraphics(PoseStack pose);
}
