package net.exaltedlynx.auguracy.common.network;

import io.netty.buffer.ByteBuf;
import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementLevels;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncElementLevelsPacket(ElementLevels levels) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SyncElementLevelsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "element_levels"));

    public static final StreamCodec<ByteBuf, SyncElementLevelsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(ElementLevels.CODEC),
            SyncElementLevelsPacket::levels,
            SyncElementLevelsPacket::new
    );

    public static void handler(final SyncElementLevelsPacket packet, final IPayloadContext context)
    {
        Player player = Minecraft.getInstance().player;
        player.getData(AuguracyAttachments.ELEMENT_LEVELS);
        player.setData(AuguracyAttachments.ELEMENT_LEVELS, packet.levels);
    }

    @Override
    public Type<SyncElementLevelsPacket> type() {
        return TYPE;
    }
}
