package net.exaltedlynx.auguracy.common.spell;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
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

    public static Codec<Spell> CODEC = AuguracySpells.SPELL_TYPES_REGISTRY.byNameCodec().dispatch(Spell::getCodec, Function.identity());

    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = AuguracySpells.DISPATCH.get();

    protected static <S extends Spell> MapCodec<S> createSimpleCodec(Function4<String, ElementType, Integer, Integer, S> constructor) {
        return RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.fieldOf("spell_name").forGetter(spell -> spell.name),
                StringRepresentable.fromEnum(ElementType::values).fieldOf("type").forGetter(spell -> spell.type),
                Codec.INT.fieldOf("lvl_req").forGetter(spell -> spell.lvlReq),
                Codec.INT.fieldOf("mana_cost").forGetter(spell -> spell.manaCost)
        ).apply(inst, constructor));
    }

    protected static <S extends Spell> Products.P1<Mu<S>, String> startSpellCodec(RecordCodecBuilder.Instance<S> instance) {
        return instance.group(Codec.STRING.fieldOf("spell_name").forGetter(spell -> spell.name));
    }

    protected static StreamCodec<RegistryFriendlyByteBuf, ? extends Spell> createStreamCodec(Function<RegistryFriendlyByteBuf, ? extends Spell> decode)
    {
        return StreamCodec.ofMember(Spell::encode, decode::apply);
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

    private void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeUtf(this.name);
        toBuffer(buffer);
    }

    private static Spell decode(RegistryFriendlyByteBuf buffer)
    {
        Spell spell = AuguracySpells.getSpellFromName(buffer.readUtf());
        return spell.fromBuffer(buffer);
    }

    protected void toBuffer(RegistryFriendlyByteBuf buffer) { }
    protected abstract Spell fromBuffer(RegistryFriendlyByteBuf buffer);

    public boolean cast(Player caster) {
        boolean casted = false;
        if(canCast(caster))
        {
            if(this instanceof ICorruptable corruptedSpell && corruptedSpell.isCorrupted())
                casted = corruptedSpell.corruptedCast(caster);
            else
                casted = onCast(caster);
        }
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

    protected abstract MapCodec<? extends Spell> getCodec();

    protected abstract StreamCodec<RegistryFriendlyByteBuf, ? extends Spell> getStreamCodec();

    public abstract Spell newSpellInstance();

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
