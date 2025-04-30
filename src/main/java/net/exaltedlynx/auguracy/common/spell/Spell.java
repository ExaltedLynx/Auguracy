package net.exaltedlynx.auguracy.common.spell;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.*;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Spell
{
    protected final String name;
    protected final ElementType type;
    protected final int lvlReq;
    protected final int manaCost;

    public static final Codec<Spell> CODEC = AuguracySpells.SPELL_TYPES_REGISTRY.byNameCodec().dispatch(Spell::spellType, SpellType::codec);

    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = StreamCodec
            .of(RegistryFriendlyByteBuf::writeResourceLocation, RegistryFriendlyByteBuf::readResourceLocation)
            .map(AuguracySpells.SPELL_TYPES_REGISTRY::getValue, AuguracySpells.SPELL_TYPES_REGISTRY::getKey)
            .dispatch(Spell::spellType, SpellType::streamCodec);

    protected static <S extends Spell> MapCodec<S> createSimpleCodec(Function<String, S> constructor) {
        return RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.fieldOf("spell_name").forGetter(Spell::getName)
        ).apply(inst, constructor));
    }

    protected static <S extends Spell> Products.P1<Mu<S>, String> startSpellCodec(RecordCodecBuilder.Instance<S> instance) {
        return instance.group(Codec.STRING.fieldOf("spell_name").forGetter(Spell::getName));
    }

    protected static <S extends Spell> StreamCodec<RegistryFriendlyByteBuf, S> createSimpleStreamCodec(Function<String, S> constructor)
    {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, spell -> spell.name,
                constructor
        );
    }

    protected static <S extends Spell> StreamCodec<RegistryFriendlyByteBuf, S> createStreamCodec(
            StreamEncoder<RegistryFriendlyByteBuf, S> toBuffer, StreamDecoder<RegistryFriendlyByteBuf, S> fromBuffer)
    {
        return StreamCodec.of(toBuffer, fromBuffer);
    }

    protected Spell(String name, ElementType type, int lvlReq, int manaCost)
    {
        this.name = name;
        this.type = type;
        this.lvlReq = lvlReq;
        this.manaCost = manaCost;
    }

    protected Spell(String spellName)
    {
        Spell spell = AuguracySpells.getSpellFromName(spellName);
        this.name = spell.name;
        this.type = spell.type;
        this.lvlReq = spell.lvlReq;
        this.manaCost = spell.manaCost;
    }

    public boolean cast(Player caster) {
        boolean casted = false;
        if(canCast(caster))
        {
            if(this instanceof ICorruptable corruptedSpell && corruptedSpell.isCorrupted())
                casted = corruptedSpell.corruptedCast(caster);
            else
                casted = onCast(caster);
        }
        if(casted)
            caster.getData(AuguracyAttachments.MANA).subtract(manaCost, caster);

        return casted;
    }

    protected abstract boolean onCast(Player caster);
    public void onCastRelease(Player caster) { }

    private boolean canCast(Player caster)
    {
        return lvlReq <= caster.getData(AuguracyAttachments.ELEMENT_LEVELS).getLevel(type) && manaCost <= caster.getData(AuguracyAttachments.MANA).getCurrentMana();
    }

    public void addTooltipInfo(Consumer<Component> tooltipAdder)
    {
        tooltipAdder.accept(Component.translatable("spell.auguracy.name").append(this.name));
    }

    public String getName() { return name; }
    public ElementType getType() { return type; }
    public int getLvlReq() { return lvlReq; }
    public int getManaCost() { return manaCost; }

    protected abstract SpellType spellType();

    public abstract Spell newSpellInstance();

    //public abstract ResourceLocation registryKey();

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spell spell)) return false;
        return lvlReq == spell.lvlReq && manaCost == spell.manaCost && Objects.equals(name, spell.name) && type == spell.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, lvlReq, manaCost);
    }
}
