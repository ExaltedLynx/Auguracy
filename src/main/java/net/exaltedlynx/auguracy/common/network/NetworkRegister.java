package net.exaltedlynx.auguracy.common.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkRegister
{
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(SyncElementLevelsPacket.TYPE, SyncElementLevelsPacket.STREAM_CODEC, SyncElementLevelsPacket::handler);
    }
}
