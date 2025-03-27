package net.exaltedlynx.auguracy.common.network;

import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementLevels;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkRegister
{
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(SyncElementLevelsPacket.TYPE, SyncElementLevelsPacket.STREAM_CODEC, );
    }
}
