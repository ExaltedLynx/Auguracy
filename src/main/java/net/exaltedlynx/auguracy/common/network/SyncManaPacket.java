package net.exaltedlynx.auguracy.common.network;

import io.netty.buffer.ByteBuf;
import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.mana.Mana;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncManaPacket(Mana mana)  implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SyncManaPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "mana"));

    public static final StreamCodec<ByteBuf, SyncManaPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Mana.CODEC),
            SyncManaPacket::mana,
            SyncManaPacket::new
    );

    public static void handler(final SyncManaPacket packet, final IPayloadContext context)
    {
        Player player = Minecraft.getInstance().player;
        player.getData(AuguracyAttachments.MANA);
        player.setData(AuguracyAttachments.MANA, packet.mana);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
