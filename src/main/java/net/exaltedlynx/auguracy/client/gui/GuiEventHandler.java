package net.exaltedlynx.auguracy.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class GuiEventHandler
{
    @SubscribeEvent
    public static void onPlayerInventoryGuiInit(ScreenEvent.Render.Post event)
    {
        Screen screen = event.getScreen();
        if(screen instanceof InventoryScreen)
        {
            new ElementsScreen(Component.literal("Test")).render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
        }
    }
}
