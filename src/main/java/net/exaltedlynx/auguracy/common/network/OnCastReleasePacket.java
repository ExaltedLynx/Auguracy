package net.exaltedlynx.auguracy.common.network;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OnCastReleasePacket(Spell spell) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<OnCastReleasePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "on_cast_release"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OnCastReleasePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(Spell.CODEC),
            OnCastReleasePacket::spell,
            OnCastReleasePacket::new
    );

    public static void handler(final OnCastReleasePacket packet, final IPayloadContext context)
    {
        context.enqueueWork(() -> packet.spell.onCastRelease(context.player()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
