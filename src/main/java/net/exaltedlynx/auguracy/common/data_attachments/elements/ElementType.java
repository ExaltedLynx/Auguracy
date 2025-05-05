package net.exaltedlynx.auguracy.common.data_attachments.elements;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum ElementType implements StringRepresentable
{
    EARTH,
    FIRE,
    WATER,
    WIND,
    CORRUPTION;

    public static final Codec<ElementType> CODEC = StringRepresentable.fromValues(ElementType::values);
    public static final StreamCodec<FriendlyByteBuf, ElementType> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(ElementType.class);
    private static final Map<ElementType, Integer> ELEMENT_COLORS = new HashMap<>(5);

    private static final ElementType[] VALUES = ElementType.values();

    public static ElementType fromInt(int x)
    {
        return VALUES[x];
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.toString();
    }

    public static void initElementColorMap()
    {
        ELEMENT_COLORS.put(EARTH, 0x008000);
        ELEMENT_COLORS.put(FIRE, 0xFF2400);
        ELEMENT_COLORS.put(WATER, 0x0041C2);
        ELEMENT_COLORS.put(WIND, 0xFFEE8C);
        ELEMENT_COLORS.put(CORRUPTION, 0x4c00b0);
    }

    public static int getElementColor(ElementType type)
    {
        return ELEMENT_COLORS.get(type);
    }
}
