package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.containers.WandMenu;
import net.exaltedlynx.auguracy.common.items.DivineWand;
import net.minecraft.core.component.DataComponents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Auguracy.MODID)
public class AuguracyCapabilities
{
    private static void registerItemCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerItem(
                DivineWand.WAND_ITEM_HANDLER,
                (stack, context) -> new ComponentItemHandler(stack, DataComponents.CONTAINER, WandMenu.INV_SIZE) {
                    @Override
                    public int getSlotLimit(int slot) {
                        return 1;
                    }
                },
                AuguracyItems.DIVINE_WAND
        );
    }

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event)
    {
        registerItemCapabilities(event);
    }
}
