package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellCrystal extends Item
{
    public SpellCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player)
    {
        stack.set(AuguracyDataComponents.SPELL_CONTAINER, SpellContainer.EMPTY.get());
    }
}
