package net.exaltedlynx.auguracy.common.data_attachments;

import net.minecraft.world.entity.player.Player;

import java.lang.annotation.ElementType;

public class ElementExp
{
    private final int XP_LIMIT_BASE = 10;

    private int[] exp = new int[5];
    private int xpLimit = XP_LIMIT_BASE;

    public void addExp(ElementType type, int amount, Player pContext)
    {
        exp[type.ordinal()] += amount;
    }
}
