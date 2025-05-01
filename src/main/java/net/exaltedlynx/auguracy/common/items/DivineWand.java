package net.exaltedlynx.auguracy.common.items;

import net.exaltedlynx.auguracy.common.containers.WandMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class DivineWand extends Item implements IItemExtension
{
    private NonNullList<ItemStack> items = NonNullList.withSize(WandMenu.INV_SIZE, ItemStack.EMPTY);

    public DivineWand(Properties properties) {
        super(properties);
    }

    private final IItemHandler inventory = new ItemStackHandler(items);

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
                //cast selected spell
            }
        }
        return super.use(level, player, hand);
    }
}
