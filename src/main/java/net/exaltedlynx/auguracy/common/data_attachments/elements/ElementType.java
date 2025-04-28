package net.exaltedlynx.auguracy.common.data_attachments.elements;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ElementType implements StringRepresentable
{
    EARTH,
    FIRE,
    WATER,
    WIND,
    CORRUPTION;

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
