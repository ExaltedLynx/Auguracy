package net.exaltedlynx.auguracy.common.data_attachments.elements;

public enum ElementType
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
}
