package net.exaltedlynx.auguracy.common.network;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncSpellContainerPacket(SpellContainer container, ItemStack stack) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncSpellContainerPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "sync_spell_container"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSpellContainerPacket> STREAM_CODEC = StreamCodec.composite(
            SpellContainer.STREAM_CODEC,
            SyncSpellContainerPacket::container,
            ItemStack.STREAM_CODEC,
            SyncSpellContainerPacket::stack,
            SyncSpellContainerPacket::new
    );

    public static void handler(final SyncSpellContainerPacket packet, final IPayloadContext context)
    {
        packet.stack.set(AuguracyDataComponents.SPELL_CONTAINER, packet.container);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
