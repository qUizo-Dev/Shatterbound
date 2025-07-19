package net.quizo.shatterbound.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.quizo.shatterbound.client.events.ClientEvents;
import net.quizo.shatterbound.client.render.AquaticEffectRenderer;

import static net.quizo.shatterbound.Shatterbound.LOGGER;

public class AbyssCallerItem extends Item {
    public AbyssCallerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            AquaticEffectRenderer.toggleEffect();
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }



    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}