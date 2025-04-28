package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.common.network.SyncSpellContainerPacket;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class SpellItem extends Item implements IItemExtension
{
    private Spell currentSpell;
    boolean isConsumable;

    public SpellItem(Properties properties, boolean isConsumable)
    {
        super(properties);
        this.isConsumable = isConsumable;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player)
    {
        if(!stack.isEmpty() && stack.has(AuguracyDataComponents.SPELL_CONTAINER))
        {
            currentSpell = stack.get(AuguracyDataComponents.SPELL_CONTAINER).getSpell();
            if(!level.isClientSide)
            {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncSpellContainerPacket(SpellContainer.EMPTY.get(), stack));
            }
        }
    }

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
                currentSpell.cast(player);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count)
    {
        if(entity instanceof Player player && !player.level().isClientSide)
        {
            currentSpell.onCastRelease(player);
        }
        if(isConsumable)
            stack.consume(1, null);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        SpellContainer spellContainer = stack.get(AuguracyDataComponents.SPELL_CONTAINER);
        if(spellContainer != null)
            spellContainer.addToTooltip(context, tooltipComponents::add, tooltipFlag);
    }
}
