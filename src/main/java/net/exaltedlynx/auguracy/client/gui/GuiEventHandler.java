package net.exaltedlynx.auguracy.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public class GuiEventHandler
{
    public static void onPlayerInventoryGuiInit(ScreenEvent.Render.Post event)
    {
        Screen screen = event.getScreen();
        if(screen instanceof InventoryScreen)
        {
            new ElementsGui().renderElementLevels(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
        }
    }

    public static void registerHUDOverlays(RegisterGuiLayersEvent event)
    {
        event.registerBelow(VanillaGuiLayers.HOTBAR, ManaBar.MANA_BAR, ManaBar::renderManaBar);
    }
}
