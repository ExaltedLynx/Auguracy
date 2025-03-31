package net.exaltedlynx.auguracy.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public abstract class Spell
{
    protected String name;
    protected ElementType type;
    protected int lvlReq;
    protected int manaCost;

    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("spell_name").forGetter(Spell::getName)
    ).apply(inst, AuguracySpells::getSpellFromName));

    public boolean Cast(Player caster) {
        if(canCast(caster))
        {
            caster.getData(AuguracyAttachments.MANA).subtract(manaCost, caster);
            if(this instanceof ICorruptable corruptedSpell)
                corruptedSpell.corruptedCast(caster);
            else
                onCast(caster);

            return true;
        }
        return false;
    }

    protected abstract void onCast(Player caster);

    private boolean canCast(Player caster)
    {
        return lvlReq < caster.getData(AuguracyAttachments.ELEMENT_LEVELS).getLevel(type) && manaCost < caster.getData(AuguracyAttachments.MANA).getCurrentMana();
    }

    public String getName() { return name; }
}
