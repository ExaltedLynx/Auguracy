package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import java.util.List;

public class SpellItem extends Item implements IItemExtension
{
    private Spell currentSpell;
    boolean consumedOnUse;

    public SpellItem(Properties properties, boolean consumedOnUse)
    {
        super(properties);
        this.consumedOnUse = consumedOnUse;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player)
    {
        if(!stack.isEmpty() && stack.has(AuguracyDataComponents.SPELL_CONTAINER))
        {
            currentSpell = stack.get(AuguracyDataComponents.SPELL_CONTAINER).getSpell();
        }
    }


    /*
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        if(currentSpell != null && player.getItemInHand(hand).is(AuguracyItems.SPELL_SCROLL) && !level.isClientSide)
        {
            boolean casted = currentSpell.cast(player);
            if(casted)
                player.getData(AuguracyAttachments.MANA).subtract(currentSpell.getManaCost(), player);
        }
        return InteractionResult.PASS;
    }
     */

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        if(player.getItemInHand(hand).is(AuguracyItems.SPELL_SCROLL) && currentSpell != null)
        {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration)
    {
        if(livingEntity instanceof Player player)
        {
            if(!level.isClientSide)
            {
                boolean casted = currentSpell.cast(player);
                if(casted)
                    player.getData(AuguracyAttachments.MANA).subtract(currentSpell.getManaCost(), player);
            }
        }
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count)
    {
        if(entity instanceof Player player && !player.level().isClientSide)
            currentSpell.onCastRelease(player);

        if(consumedOnUse)
            stack.consume(1, null);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        SpellContainer spellContainer = stack.get(AuguracyDataComponents.SPELL_CONTAINER);
        if(spellContainer != null)
            spellContainer.addToTooltip(context, tooltipComponents::add, tooltipFlag);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack) {
        super.verifyComponentsAfterLoad(stack);
        if(stack.has(AuguracyDataComponents.SPELL_CONTAINER))
            currentSpell = stack.get(AuguracyDataComponents.SPELL_CONTAINER).getSpell();
    }
}
