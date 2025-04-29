package net.exaltedlynx.auguracy.common.data_attachments.elements;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

public enum ElementType implements StringRepresentable
{
    EARTH,
    FIRE,
    WATER,
    WIND,
    CORRUPTION;

    public static final Codec<ElementType> CODEC = StringRepresentable.fromValues(ElementType::values);
    public static final StreamCodec<FriendlyByteBuf, ElementType> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(ElementType.class);

    private static final ElementType[] VALUES = ElementType.values();

    public static ElementType fromInt(int x)
    {
        return VALUES[x];
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.toString();
    }
}
