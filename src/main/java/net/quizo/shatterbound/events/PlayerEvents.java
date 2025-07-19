package net.quizo.shatterbound.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class PlayerEvents {

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            boolean isActive = player.getPersistentData().getBoolean("shatterbound_aquatic_effect");
            int timer = player.getPersistentData().getInt("shatterbound_effect_timer");

            if (isActive) {
                if (timer > 0) {
                    player.getPersistentData().putInt("shatterbound_effect_timer", timer - 1);

                    if (timer % 20 == 0) {
                        player.sendSystemMessage(
                                Component.translatable(
                                        "item.shatterbound.abysscaller.effect_active",
                                        timer / 20
                                )
                        );
                    }
                } else {
                    player.getPersistentData().putBoolean("shatterbound_aquatic_effect", false);
                    player.sendSystemMessage(
                            Component.translatable("item.shatterbound.abysscaller.effect_ended")
                    );
                }
            }
        }
    }
}