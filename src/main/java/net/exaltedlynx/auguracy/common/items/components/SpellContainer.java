package net.exaltedlynx.auguracy.common.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracySpells;

public record SpellContainer(Spell spell)
{
    public static final SpellContainer EMPTY = new SpellContainer(AuguracySpells.EMPTY.get());

    public static final Codec<SpellContainer> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Spell.CODEC.fieldOf("spell").forGetter(SpellContainer::spell))
        .apply(inst, SpellContainer::new));
}
