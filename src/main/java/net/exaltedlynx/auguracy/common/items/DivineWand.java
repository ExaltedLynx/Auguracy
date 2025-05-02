package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.common.containers.WandMenu;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.items.ItemStackHandler;

public class DivineWand extends Item implements IItemExtension
{
    private NonNullList<ItemStack> items = NonNullList.withSize(WandMenu.INV_SIZE, ItemStack.EMPTY);
    private Spell currentSpell;

    public DivineWand(Properties properties) {
        super(properties.stacksTo(1));
    }

    private final ItemStackHandler inventory = new ItemStackHandler(items)
    {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }
    };

	@Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        if(!level.isClientSide && player instanceof ServerPlayer sPlayer)
        {
            if(player.isShiftKeyDown())
            {
                sPlayer.openMenu(new SimpleMenuProvider((containerId, playerInventory, _player) ->
                        WandMenu.createServerMenu(containerId, playerInventory, inventory), Component.translatable("container.auguracy.wand")));
            }
            else
            {
                currentSpell = items.get(WandMenu.getSelectedSlot()).get(AuguracyDataComponents.SPELL_CONTAINER).getSpell();
                player.startUsingItem(hand);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration)
    {
        if(livingEntity instanceof Player player)
        {
            if(!level.isClientSide)
                currentSpell.cast(player);
        }
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count)
    {
        if(entity instanceof Player player && !player.level().isClientSide)
            currentSpell.onCastRelease(player);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return Integer.MAX_VALUE;
    }

    public ItemStackHandler getInventory()
    {
        return inventory;
    }
}
