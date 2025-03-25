package net.exaltedlynx.auguracy.common.data_attachments;

public class ElementLevels
{
    private static final int MAX_LEVEL = 50;
    private int[] levels = new int[5];

    public enum ElementType
    {
        FIRE,
        EARTH,
        WATER,
        WIND,
        CORRUPTION
    }

    public int getLevel(ElementType type)
    {
        return levels[type.ordinal()];
    }

    public void incrementLevel(ElementType type)
    {
        if(levels[type.ordinal()] < MAX_LEVEL)
            levels[type.ordinal()]++;
    }
}
