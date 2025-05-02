package net.exaltedlynx.auguracy.client.input;

import net.exaltedlynx.auguracy.common.containers.WandMenu;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;

public class ClientEventHandler
{
    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event)
    {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getItemInHand(player.getUsedItemHand());
        int scrollDirection = (int) event.getScrollDeltaY();
        if(player.isCrouching() && stack.is(AuguracyItems.DIVINE_WAND))
        {
            WandMenu.setSelectedSlot(scrollDirection);
            event.setCanceled(true);
        }
    }
}
