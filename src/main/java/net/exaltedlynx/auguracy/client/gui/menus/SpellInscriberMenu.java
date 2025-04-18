package net.exaltedlynx.auguracy.client.gui.menus;

import net.exaltedlynx.auguracy.setup.AuguracyBlocks;
import net.exaltedlynx.auguracy.setup.AuguracyMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;

public class SpellInscriberMenu extends AbstractContainerMenu
{
    private final ContainerLevelAccess access;
    //Client constructor
    public SpellInscriberMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new ItemStackHandler(7), DataSlot.standalone(), ContainerLevelAccess.NULL);
    }

    //Server Constructor
    public SpellInscriberMenu(int containerId, Inventory playerInventory, IItemHandler dataInventory, DataSlot dataSlot, ContainerLevelAccess access) {
		super(AuguracyMenus.SPELL_INSCRIBER_MENU.get(), containerId);
        this.access = access;
        this.addStandardInventorySlots(playerInventory, 8, 84);
	}

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, AuguracyBlocks.SPELL_INSCRIBER.get());
    }
}
