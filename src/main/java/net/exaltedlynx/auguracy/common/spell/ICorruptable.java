package net.exaltedlynx.auguracy.common.spell;

import net.minecraft.world.entity.player.Player;

public interface ICorruptable
{
    boolean corrupted = false;
    boolean corruptedCast(Player caster);
    default boolean isCorrupted() {return corrupted; }
}
