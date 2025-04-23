package net.exaltedlynx.auguracy.common.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public record SpellContainer(Spell spell)
{
    public static final Supplier<SpellContainer> EMPTY = () -> new SpellContainer(AuguracySpells.EMPTY.get());

    public static final Codec<SpellContainer> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Spell.CODEC.fieldOf("spell").forGetter(SpellContainer::spell))
        .apply(inst, SpellContainer::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellContainer> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodecWithRegistries(Spell.CODEC),
            SpellContainer::spell,
            SpellContainer::new
    );
}
