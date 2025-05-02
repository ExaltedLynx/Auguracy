package net.exaltedlynx.auguracy.common.containers;

import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.exaltedlynx.auguracy.setup.AuguracyMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WandMenu extends AbstractContainerMenu
{
	public static int INV_SIZE = 3;
	private static int selectedSlot = 0;
	private static final int WAND_INV_SLOTS_START = 0;
	private static final int WAND_INV_SLOTS_END = 2;
	private final int PLAYER_INV_SLOTS_START = 3;
	private final int PLAYER_INV_SLOTS_END = 29;
	private final int HOTBAR_SLOTS_START = 30;
	private final int HOTBAR_SLOTS_END = 38;

	protected WandMenu(int containerId, Inventory playerInventory, IItemHandler wandInventory)
	{
		super(AuguracyMenus.WAND_MENU.get(), containerId);

		for(int i = 0; i < INV_SIZE; i++)
		{
			this.addSlot(new SpellCrystalSlotHandler(wandInventory, i, 63 + (18 * i), 24));
		}
		this.addStandardInventorySlots(playerInventory, 9, 70);
	}

	public static WandMenu createClientMenu(int containerId, Inventory playerInventory)
	{
		return new WandMenu(containerId, playerInventory, new ItemStackHandler(INV_SIZE));
	}

	public static WandMenu createServerMenu(int containerId, Inventory playerInventory, IItemHandler wandInventory)
	{
		return new WandMenu(containerId, playerInventory, wandInventory);
	}

	public static void setSelectedSlot(int scrollDir)
	{
		switch(scrollDir)
		{
			case -1:
				if(selectedSlot != 2)
					selectedSlot++;
				else
					selectedSlot = 0;
				break;
			case 1:
				if(selectedSlot != 0)
					selectedSlot--;
				else
					selectedSlot = 2;
				break;
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex)
	{
		ItemStack quickMovedStack = ItemStack.EMPTY;
		Slot quickMovedSlot = this.slots.get(slotIndex);
		if(quickMovedSlot != null && quickMovedSlot.hasItem())
		{
			ItemStack slotItem = quickMovedSlot.getItem();
			quickMovedStack = slotItem.copy();

			//Quick moving from wand inv to player inv or hotbar
			if(slotIndex <= WAND_INV_SLOTS_END)
			{
				if(!this.moveItemStackTo(slotItem, PLAYER_INV_SLOTS_START, HOTBAR_SLOTS_END + 1, false)) //+1 since its endIndex is exclusive
					return ItemStack.EMPTY;
			}
			//Quick moving from player inv or hotbar
			else if(slotIndex < HOTBAR_SLOTS_END + 1)
			{
				//Try to quick move to wand inv slots
				if(!this.moveItemStackTo(slotItem, WAND_INV_SLOTS_START, WAND_INV_SLOTS_END + 1, false))
				{
					//If can't move item to wand, quick move from player inv to hotbar
					if(slotIndex < HOTBAR_SLOTS_START)
					{
						if(!this.moveItemStackTo(slotItem, HOTBAR_SLOTS_START, HOTBAR_SLOTS_END + 1, false))
							return ItemStack.EMPTY;
					}
					//Quick move from hotbar to player inv
					else if (!this.moveItemStackTo(slotItem, PLAYER_INV_SLOTS_START, PLAYER_INV_SLOTS_END + 1, false))
						return ItemStack.EMPTY;
				}
			}

			if(slotItem.isEmpty())
				quickMovedSlot.set(ItemStack.EMPTY);
			else
				quickMovedSlot.setChanged();

			if(slotItem.getCount() == quickMovedStack.getCount())
				return ItemStack.EMPTY;

			quickMovedSlot.onTake(player, slotItem);

		}
		return quickMovedStack;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

	public static int getSelectedSlot() { return selectedSlot; }

	private static class SpellCrystalSlotHandler extends SlotItemHandler
	{
		public SpellCrystalSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return stack.is(AuguracyItems.SPELL_CRYSTAL);
		}
	}
}
