package net.exaltedlynx.auguracy.common.data;

import net.exaltedlynx.auguracy.Auguracy;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Auguracy.MODID)
public class AuguracyDatagen
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        event.createProvider(AuguracyModelProvider::new);
    }
}
