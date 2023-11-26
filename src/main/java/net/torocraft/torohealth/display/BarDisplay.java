package net.torocraft.torohealth.display;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.torocraft.torohealth.bars.HealthBarRenderer;

public class BarDisplay {

    private static final ResourceLocation ICON_TEXTURES =
            new ResourceLocation("textures/gui/icons.png");

    private static final ResourceLocation ARMOR = new ResourceLocation("hud/armor_full");
    private static final ResourceLocation HEART = new ResourceLocation("hud/heart/full");

    private final Minecraft mc;

    public BarDisplay(Minecraft mc) {
        this.mc = mc;
    }

    private String getEntityName(LivingEntity entity) {
        return entity.getDisplayName().getString();
    }

    public void draw(GuiGraphics graphics, LivingEntity entity) {
        int xOffset = 0;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ICON_TEXTURES);
        RenderSystem.enableBlend();

        final PoseStack matrix = graphics.pose();
        HealthBarRenderer.render(graphics, entity, 63, 14, 130, false);
        String name = getEntityName(entity);
        int healthMax = Mth.ceil(entity.getMaxHealth());
        int healthCur = Math.min(Mth.ceil(entity.getHealth()), healthMax);
        String healthText = healthCur + "/" + healthMax;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        graphics.drawString(mc.font, name, xOffset, 2, 16777215);
        xOffset += mc.font.width(name) + 5;

        renderHeartIcon(graphics, xOffset, 1);
        xOffset += 10;

        graphics.drawString(mc.font, healthText, xOffset, 2, 0xe0e0e0);
        xOffset += mc.font.width(healthText) + 5;

        int armor = entity.getArmorValue();// getArmor();

        if (armor > 0) {
            renderArmorIcon(graphics, xOffset, 1);
            xOffset += 10;
            graphics.drawString(mc.font, entity.getArmorValue() + "", xOffset, 2, 0xe0e0e0);
        }
    }

    private void renderArmorIcon(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(ARMOR, x, y, 9, 9);
    }

    private void renderHeartIcon(GuiGraphics graphics, int x, int y) {
        graphics.blitSprite(HEART, x, y, 9, 9);
    }
}
