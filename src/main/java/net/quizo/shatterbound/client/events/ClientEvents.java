package net.quizo.shatterbound.client.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.quizo.shatterbound.Shatterbound;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    public static ShaderInstance aquaticShader;
    private static float intensity = 0f;
    private static final float TRANSITION_SPEED = 0.05f;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            Player player = Minecraft.getInstance().player;
            boolean active = player != null &&
                    player.getPersistentData().getBoolean("shatterbound_aquatic_effect");

            // Tranziție graduală
            intensity = active ?
                    Math.min(intensity + TRANSITION_SPEED, 1.0f) :
                    Math.max(intensity - TRANSITION_SPEED, 0.0f);

            if (intensity > 0) {
                applyAquaticEffect();
            }
        }
    }

    private static void applyAquaticEffect() {
        if (aquaticShader == null) return;

        RenderSystem.depthMask(false);
        RenderSystem.disableBlend();

        aquaticShader.apply();
        try {
            // Setează uniform-urile
            aquaticShader.safeGetUniform("Time").set(Minecraft.getInstance().getFrameTimeNs());
            aquaticShader.safeGetUniform("Intensity").set(intensity);

            // Aici se desenează efectul pe tot ecranul
            // (implementare specifică în funcție de motorul de randare)

        } finally {
            aquaticShader.clear();
            RenderSystem.depthMask(true);
        }
    }

    public static void toggleEffect(Player player, boolean active) {
        player.getPersistentData().putBoolean("shatterbound_aquatic_effect", active);
    }
}