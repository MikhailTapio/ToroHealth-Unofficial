package net.torocraft.torohealth;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.torocraft.torohealth.api.IGuiGraphicsGetter;
import net.torocraft.torohealth.bars.BarStates;
import net.torocraft.torohealth.bars.HealthBarRenderer;
import net.torocraft.torohealth.bars.ParticleRenderer;
import net.torocraft.torohealth.util.HoldingWeaponUpdater;
import net.torocraft.torohealth.util.RayTrace;

public class ClientEventHandler {

    public static void init(IEventBus bus) {
        NeoForge.EVENT_BUS.addListener(ClientEventHandler::playerTick);
        NeoForge.EVENT_BUS.addListener(ClientEventHandler::entityRender);
        NeoForge.EVENT_BUS.addListener(ClientEventHandler::renderParticles);
        bus.addListener(ClientEventHandler::registerOverlays);
    }

    private static void registerOverlays(final RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.POTION_ICONS.id(), new ResourceLocation(ToroHealth.MODID, "torohealth_hud"), ToroHealthClient.HUD::draw);
    }

    private static void entityRender(
            RenderLivingEvent.Post<? extends LivingEntity, ? extends EntityModel<?>> event) {
        HealthBarRenderer.prepareRenderInWorld(event.getEntity());
    }

    private static void renderParticles(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            final GuiGraphics graphics = ((IGuiGraphicsGetter) Minecraft.getInstance()).getGuiGraphics(event.getPoseStack());
            ParticleRenderer.renderParticles(graphics, camera);
            HealthBarRenderer.renderInWorld(event.getPartialTick(), graphics, camera);
            graphics.flush();
        }
    }

    private static void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide) return;
        ToroHealthClient.HUD.setEntity(RayTrace.getEntityInCrosshair(0, ToroHealth.CONFIG.hud.distance));
        BarStates.tick();
        HoldingWeaponUpdater.update();
        ToroHealthClient.HUD.tick();
    }
}
