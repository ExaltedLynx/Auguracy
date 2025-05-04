package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.containers.WandMenu;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.items.ComponentItemHandler;

public class DivineWand extends Item implements IItemExtension
{
    private Spell currentSpell;

    public static final ItemCapability<ComponentItemHandler, Void> WAND_ITEM_HANDLER = ItemCapability.createVoid(
            ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "wand_item_handler"),
            ComponentItemHandler.class
    );

    public DivineWand(Properties properties) {
        super(properties.stacksTo(1).component(AuguracyDataComponents.WAND_ITEM_HANDLER, ItemContainerContents.EMPTY));
    }

	@Override
    public InteractionResult use(Level level, Player player, InteractionHand hand)
    {
        if(!level.isClientSide && player instanceof ServerPlayer sPlayer)
        {
            var items = player.getItemInHand(hand).getCapability(WAND_ITEM_HANDLER, null);
            if(player.isShiftKeyDown())
            {
                sPlayer.openMenu(new SimpleMenuProvider((containerId, playerInventory, _player) ->
                        WandMenu.createServerMenu(containerId, playerInventory, items), Component.translatable("container.auguracy.wand")));
            }
            else
            {
                currentSpell = items.getStackInSlot(WandMenu.getSelectedSlot()).get(AuguracyDataComponents.SPELL_CONTAINER).getSpell();
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
}
