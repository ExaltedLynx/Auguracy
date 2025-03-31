package net.exaltedlynx.auguracy.common.spell;

import net.minecraft.world.entity.player.Player;

public interface ICorruptable
{
    boolean corrupted = false;
    void corruptedCast(Player caster);
    default boolean isCorrupted() {return corrupted; }
}
