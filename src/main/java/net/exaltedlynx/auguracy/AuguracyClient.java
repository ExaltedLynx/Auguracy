package net.exaltedlynx.auguracy;

import net.exaltedlynx.auguracy.client.gui.GuiEventHandler;
import net.exaltedlynx.auguracy.client.input.ClientInputHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Auguracy.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AuguracyClient
{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        NeoForge.EVENT_BUS.register(ClientInputHandler.class);
        NeoForge.EVENT_BUS.addListener(GuiEventHandler::onPlayerInventoryGuiInit);
    }
}
