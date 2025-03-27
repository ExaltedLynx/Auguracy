package net.exaltedlynx.auguracy.common.network;

import io.netty.buffer.ByteBuf;
import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementLevels;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncElementLevelsPacket(ElementLevels levels) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SyncElementLevelsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "element_levels"));

    public static final StreamCodec<ByteBuf, ElementLevels> STREAM_CODEC = ByteBufCodecs.fromCodec(ElementLevels.CODEC);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
