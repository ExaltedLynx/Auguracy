package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemExtension;

public class SpellScroll extends Item implements IItemExtension
{
    public SpellScroll(Properties properties)
    {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player)
    {
        stack.set(AuguracyDataComponents.SPELL_CONTAINER, SpellContainer.EMPTY.get());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(heldItem.has(AuguracyDataComponents.SPELL_CONTAINER))
        {
            Spell spell = heldItem.get(AuguracyDataComponents.SPELL_CONTAINER).spell();
            if(!level.isClientSide && player instanceof ServerPlayer sPlayer)
                spell.cast(sPlayer);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity instanceof Player player)
        {
            if(stack.has(AuguracyDataComponents.SPELL_CONTAINER))
            {
                Spell spell = stack.get(AuguracyDataComponents.SPELL_CONTAINER).spell();
                if(!level.isClientSide && player instanceof ServerPlayer sPlayer)
                {
                    spell.cast(sPlayer);
                }
            }
        }
    }
}