package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IItemExtension;

public class SpellScroll extends Item implements IItemExtension
{
    private Spell currentSpell;

    public SpellScroll(Properties properties)
    {
        super(properties.stacksTo(1).enchantable(0));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player)
    {
        stack.set(AuguracyDataComponents.SPELL_CONTAINER, SpellContainer.EMPTY.get());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if(stack.has(AuguracyDataComponents.SPELL_CONTAINER) && currentSpell == null)
        {
            currentSpell = stack.get(AuguracyDataComponents.SPELL_CONTAINER).spell();
        }
        if(currentSpell != null)
        {
            player.startUsingItem(hand);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity instanceof Player player && player.tickCount % 2 == 0)
        {
            if(!level.isClientSide) {
                    currentSpell.cast(player);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if(entity instanceof Player player && !player.level().isClientSide)
        {
            currentSpell.onCastRelease(player);
        }
    }

    /*
    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if(!level.isClientSide && entity instanceof Player player)
        {
            currentSpell.onCastRelease(player);
        }
        return false;
    }

     */
}