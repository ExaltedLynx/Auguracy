package net.exaltedlynx.auguracy.common.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpellContainer implements TooltipProvider
{
    private final Spell spell;

    public static final Supplier<SpellContainer> EMPTY = () -> new SpellContainer(AuguracySpells.EMPTY.get());

    public static final Codec<SpellContainer> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Spell.CODEC.fieldOf("spell").forGetter(container -> container.spell))
        .apply(inst, SpellContainer::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellContainer> STREAM_CODEC = StreamCodec.composite(
            Spell.STREAM_CODEC,
            container -> container.spell,
            SpellContainer::new
    );

    private SpellContainer(Spell spell)
    {
        this.spell = spell.newSpellInstance();
    }

    public SpellContainer setNewSpell(Spell spell)
    {
        return new SpellContainer(spell.newSpellInstance());
    }

    public Spell getSpell()
    {
        return this.spell.newSpellInstance();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
            spell.addTooltipInfo(tooltipAdder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpellContainer that)) return false;
        return spell.equals(that.spell);
    }

    @Override
    public int hashCode() {
        return spell.hashCode();
    }
}
