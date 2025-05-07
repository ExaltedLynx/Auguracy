package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.containers.WandMenu;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.items.ComponentItemHandler;

import java.util.List;

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

            if(!player.isCrouching())
            {
                currentSpell = items.getStackInSlot(WandMenu.getSelectedSlot()).get(AuguracyDataComponents.SPELL_CONTAINER).getSpell();
                player.startUsingItem(hand);
                return InteractionResult.CONSUME;
            }
            else
            {
                sPlayer.openMenu(new SimpleMenuProvider((containerId, playerInventory, _player) ->
                        WandMenu.createServerMenu(containerId, playerInventory, items), Component.translatable("container.auguracy.wand")));
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

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
    {
        ComponentItemHandler itemHandler = stack.getCapability(WAND_ITEM_HANDLER, null);
        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            ItemStack spellItem = itemHandler.getStackInSlot(i);
            if(!spellItem.isEmpty())
            {
                SpellContainer spellContainer = spellItem.get(AuguracyDataComponents.SPELL_CONTAINER);
                if(spellContainer != null)
                {
                    Spell spell = spellContainer.getSpell();
                    tooltipComponents.add(Component.literal(spell.getName()).withColor(ElementType.getElementColor(spell.getType())));
                    if(tooltipFlag.hasShiftDown())
                        spellContainer.addToTooltip(context, tooltipComponents::add, tooltipFlag);
                }
            }
        }
        if(!tooltipFlag.hasShiftDown())
            tooltipComponents.add(Component.translatable("tooltip.auguracy.more_info").withColor(0xFFEE8C));
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return true;
    }
}
