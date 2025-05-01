package net.exaltedlynx.auguracy.common.containers;

import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.exaltedlynx.auguracy.setup.AuguracyMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WandMenu extends AbstractContainerMenu
{
	public static int INV_SIZE = 3;

	protected WandMenu(int containerId, Inventory playerInventory, IItemHandler wandInventory)
	{
		super(AuguracyMenus.WAND_MENU.get(), containerId);

		for(int i = 0; i < INV_SIZE; i++)
		{
			this.addSlot(new SpellCrystalSlotHandler(wandInventory, i, 63 + (18 * i), 24 + (18 * i)));
		}
		this.addStandardInventorySlots(playerInventory, 8, 70);
	}

	public static WandMenu createClientMenu(int containerId, Inventory playerInventory)
	{
		return new WandMenu(containerId, playerInventory, new ItemStackHandler(INV_SIZE));
	}

	public static WandMenu createServerMenu(int containerId, Inventory playerInventory, IItemHandler wandInventory)
	{
		return new WandMenu(containerId, playerInventory, wandInventory);
	}



	@Override
	public ItemStack quickMoveStack(Player player, int index)
	{
		return null;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

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
