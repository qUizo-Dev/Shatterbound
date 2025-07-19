package net.quizo.shatterbound.client.render;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@OnlyIn(Dist.CLIENT)
public class AquaticEffectRenderer {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static ShaderInstance aquaticShader;
    private static boolean isActive = false;
    private static float intensity = 0f;
    private static final float[] DARK_BLUE = {0.05f, 0.15f, 0.3f}; // RGB for dark blue

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            // Smooth transition
            intensity = isActive ?
                    Math.min(intensity + 0.02f, 1.0f) :
                    Math.max(intensity - 0.02f, 0.0f);

            if (intensity > 0) {
                renderSkyEffect();
            }
        }
    }

    public static void renderSkyEffect() {
        if (aquaticShader == null) {
            LOGGER.error("Shader not loaded!");
            return;
        }

        // Setup render state
        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();

        // Apply shader
        RenderSystem.setShader(() -> aquaticShader);
        aquaticShader.apply();

        // Set shader uniforms
        Uniform intensityUniform = aquaticShader.getUniform("Intensity");
        Uniform skyColorUniform = aquaticShader.getUniform("SkyColor");

        if (intensityUniform != null) intensityUniform.set(intensity);
        if (skyColorUniform != null) skyColorUniform.set(DARK_BLUE);

        // Draw full-screen effect
        Matrix4f matrix = new Matrix4f().ortho(0.0F, 1.0F, 1.0F, 0.0F, -1.0F, 1.0F);
        RenderSystem.setProjectionMatrix(matrix, VertexSorting.ORTHOGRAPHIC_Z);

        // Draw full-screen quad using your preferred syntax
        PoseStack poseStack = new PoseStack();
        poseStack.last().pose().set(matrix);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // Your preferred vertex syntax
        buffer.addVertex(poseStack.last().pose(), 0.0F, 1.0F, 0.0F)
                .setUv(0.0F, 1.0F);
        buffer.addVertex(poseStack.last().pose(), 1.0F, 1.0F, 0.0F)
                .setUv(1.0F, 1.0F);
        buffer.addVertex(poseStack.last().pose(), 1.0F, 0.0F, 0.0F)
                .setUv(1.0F, 0.0F);
        buffer.addVertex(poseStack.last().pose(), 0.0F, 0.0F, 0.0F)
                .setUv(0.0F, 0.0F);

        // End and draw
        BufferUploader.draw(buffer.build());

        // Cleanup
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    public static void toggleEffect() {
        isActive = !isActive;
        LOGGER.info("Aquatic effect toggled: {}", isActive);

        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.displayClientMessage(
                    Component.literal("Dark Sky Effect " + (isActive ? "ON" : "OFF")),
                    true
            );
        }
    }

    public static void setAquaticShader(ShaderInstance shader) {
        aquaticShader = shader;
        LOGGER.info("Aquatic shader initialized");
    }
}