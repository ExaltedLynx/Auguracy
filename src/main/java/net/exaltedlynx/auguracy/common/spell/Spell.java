package net.exaltedlynx.auguracy.common.spell;

import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.minecraft.world.entity.player.Player;

public abstract class Spell
{
    String name;
    ElementType type;
    int manaCost;

    public boolean Cast(Player caster) {
        if(canCast(caster))
        {
            onCast();
            caster.getData(AuguracyAttachments.MANA).subtract(manaCost, caster);
            return true;
        }

        return false;
    }

    protected abstract void onCast();

    private boolean canCast(Player caster)
    {
        return manaCost <= caster.getData(AuguracyAttachments.MANA).getCurrentMana();
    }
}
