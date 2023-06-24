package net.torocraft.torohealth.bars;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.torocraft.torohealth.ToroHealth;
import org.lwjgl.opengl.GL11;

public class ParticleRenderer {

    public static void renderParticles(GuiGraphics graphics, Camera camera) {
        for (BarParticle p : BarStates.PARTICLES) {
            renderParticle(graphics, p, camera);
        }
    }

    private static void renderParticle(GuiGraphics graphics, BarParticle particle, Camera camera) {
        double distanceSquared = camera.getPosition().distanceToSqr(particle.x, particle.y, particle.z);
        if (distanceSquared > ToroHealth.CONFIG.particle.distanceSquared) {
            return;
        }

        float scaleToGui = 0.025f;

        Minecraft client = Minecraft.getInstance();
        float tickDelta = client.getDeltaFrameTime();

        double x = Mth.lerp((double) tickDelta, particle.xPrev, particle.x);
        double y = Mth.lerp((double) tickDelta, particle.yPrev, particle.y);
        double z = Mth.lerp((double) tickDelta, particle.zPrev, particle.z);

        Vec3 camPos = camera.getPosition();
        double camX = camPos.x;
        double camY = camPos.y;
        double camZ = camPos.z;

        final PoseStack matrix = graphics.pose();

        matrix.pushPose();
        matrix.translate(x - camX, y - camY, z - camZ);
        matrix.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        matrix.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
                GL11.GL_ZERO);

        HealthBarRenderer.drawDamageNumber(graphics, particle.damage, 0, 0, 10);

        RenderSystem.disableBlend();

        matrix.popPose();
    }
}
