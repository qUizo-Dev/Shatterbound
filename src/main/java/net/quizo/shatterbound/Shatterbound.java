package net.quizo.shatterbound;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.quizo.shatterbound.client.events.ClientEvents;
import net.quizo.shatterbound.events.PlayerEvents;
import net.quizo.shatterbound.items.ModItems;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.io.IOException;

@Mod(Shatterbound.MOD_ID)
public class Shatterbound {
    public static final String MOD_ID = "shatterbound";
    public static final Logger LOGGER = LogUtils.getLogger();
    private final IEventBus modEventBus;

    public Shatterbound(IEventBus modEventBus, ModContainer modContainer) {
        this.modEventBus = modEventBus;
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        ModItems.register(modEventBus);
        NeoForge.EVENT_BUS.register(new PlayerEvents());
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Shatterbound initialized!");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.ABYSS_CALLER);
        }
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register the shader loading handler
            this.modEventBus.addListener((RegisterShadersEvent shaderEvent) -> {
                try {
                    ResourceLocation shaderLoc = ResourceLocation.fromNamespaceAndPath(
                            Shatterbound.MOD_ID,
                            "aquatic"
                    );

                    shaderEvent.registerShader(
                            new ShaderInstance(
                                    shaderEvent.getResourceProvider(),
                                    shaderLoc,
                                    DefaultVertexFormat.POSITION_TEX_COLOR
                            ),
                            shader -> {
                                ClientEvents.aquaticShader = shader;
                                LOGGER.info("Shader loaded successfully from: {}", shaderLoc);
                            }
                    );
                } catch (IOException e) {
                    LOGGER.error("Failed to load shader", e);
                }
            });

            // Register rendering handler
            NeoForge.EVENT_BUS.addListener(ClientEvents::onRenderLevelStage);
        });
    }

}