package net.exaltedlynx.auguracy.common.spell;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public abstract class Spell
{
    protected String name;
    protected ElementType type;
    protected int lvlReq;
    protected int manaCost;

    public static Codec<Spell> CODEC = AuguracySpells.SPELL_TYPES_REGISTRY.byNameCodec().dispatch(Spell::getCodec, Function.identity());

    public static final MapCodec<Spell> SIMPLE_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.STRING.fieldOf("spell_name").forGetter(Spell::getName)
    ).apply(inst, AuguracySpells::getSpellFromName));

    protected static <S extends Spell> Products.P1<Mu<S>, String> startSpellCodec(RecordCodecBuilder.Instance<S> instance) {
        return instance.group(Codec.STRING.fieldOf("spell_name").forGetter(Spell::getName));
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
        return casted;
    }

    protected abstract boolean onCast(Player caster);

    public void onCastRelease(Player caster) { }

    private boolean canCast(Player caster)
    {
        return lvlReq <= caster.getData(AuguracyAttachments.ELEMENT_LEVELS).getLevel(type) && manaCost <= caster.getData(AuguracyAttachments.MANA).getCurrentMana();
    }

    public String getName() { return name; }

    protected abstract MapCodec<? extends Spell> getCodec();
}
