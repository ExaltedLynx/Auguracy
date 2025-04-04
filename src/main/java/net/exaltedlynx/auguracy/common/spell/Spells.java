package net.exaltedlynx.auguracy.common.spell;

import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.Tool;

public class Spells
{
    public static class DigSpell extends Spell implements ICorruptable
    {
        private Tool toolComponent;

        public DigSpell(String name, ElementType type, int lvlReq, int manaCost)
        {
            this.name = name;
            this.type = type;
            this.lvlReq = lvlReq;
            this.manaCost = manaCost;
        }

        @Override
        protected void onCast(Player caster) {
            
        }

        @Override
        public void corruptedCast(Player caster) {

        }

        public void setToolComponent(Tool fromToolIngredient)
        {
            toolComponent = fromToolIngredient;
        }
    }
}
