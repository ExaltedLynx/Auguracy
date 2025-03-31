package net.exaltedlynx.auguracy.common.items;


import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.extensions.IItemExtension;

public class SpellScroll extends Item implements IItemExtension
{
    public SpellScroll(Properties properties)
    {
        super(properties.component(AuguracyDataComponents.SPELL_CONTAINER, SpellContainer.EMPTY));
    }
}