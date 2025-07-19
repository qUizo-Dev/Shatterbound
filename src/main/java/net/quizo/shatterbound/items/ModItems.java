package net.quizo.shatterbound.items;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.bus.api.IEventBus;
import net.quizo.shatterbound.Shatterbound;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Shatterbound.MOD_ID);

    public static final DeferredItem<Item> ABYSS_CALLER = ITEMS.register(
            "abysscaller",
            () -> new AbyssCallerItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(100)
            )
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}