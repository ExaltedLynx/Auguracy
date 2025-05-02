package net.exaltedlynx.auguracy.client.gui;

import net.exaltedlynx.auguracy.client.gui.hud.ElementsGui;
import net.exaltedlynx.auguracy.client.gui.hud.ManaBar;
import net.exaltedlynx.auguracy.client.gui.hud.SpellSelector;
import net.exaltedlynx.auguracy.client.gui.screens.SpellInscriberScreen;
import net.exaltedlynx.auguracy.client.gui.screens.WandScreen;
import net.exaltedlynx.auguracy.setup.AuguracyMenus;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
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
        event.registerBelow(VanillaGuiLayers.HOTBAR, SpellSelector.SPELL_SELECTOR, SpellSelector::renderSpellSelector);
    }

    public static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(AuguracyMenus.SPELL_INSCRIBER_MENU.get(), SpellInscriberScreen::new);
        event.register(AuguracyMenus.WAND_MENU.get(), WandScreen::new);
    }
}
