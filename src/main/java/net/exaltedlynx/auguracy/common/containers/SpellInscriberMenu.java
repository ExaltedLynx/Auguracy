package net.exaltedlynx.auguracy.common.containers;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.blocks.blockentities.SpellInscriberEntity;
import net.exaltedlynx.auguracy.common.items.SpellItem;
import net.exaltedlynx.auguracy.common.recipe.SpellInscriberInput;
import net.exaltedlynx.auguracy.common.recipe.SpellInscriberRecipe;
import net.exaltedlynx.auguracy.setup.AuguracyBlocks;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.exaltedlynx.auguracy.setup.AuguracyMenus;
import net.exaltedlynx.auguracy.setup.AuguracyRecipes;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;
import java.util.Optional;

public class SpellInscriberMenu extends AbstractContainerMenu
{
    public static final int INV_SIZE = 7;
    private final ContainerLevelAccess access;
    private final Player player;
    private SpellInscriberEntity inscriberEntity;
    private SpellInscriberInput inscriberInput;

    //Client constructor
    public SpellInscriberMenu(int containerId, Inventory playerInventory)
    {
        this(containerId, playerInventory, new ItemStackHandler(INV_SIZE), ContainerLevelAccess.NULL);
    }

    //Server Constructor
    public SpellInscriberMenu(int containerId, Inventory playerInventory, IItemHandler dataInventory, ContainerLevelAccess access)
    {
		super(AuguracyMenus.SPELL_INSCRIBER_MENU.get(), containerId);
        validateInventorySize(dataInventory);
        this.access = access;
        this.player = playerInventory.player;
        RetrieveInscriberBlockEntity(access);
        this.addSlot(new SpellItemSlotHandler(dataInventory, 0, 26, 57));
        this.addSlot(new SlotItemHandler(dataInventory, 1, 75, 11));
        this.addSlot(new SlotItemHandler(dataInventory, 2, 64, 34));
        this.addSlot(new SlotItemHandler(dataInventory, 3, 53, 57));
        this.addSlot(new SlotItemHandler(dataInventory, 4, 64, 80));
        this.addSlot(new SlotItemHandler(dataInventory, 5, 75, 103));
        this.addSlot(new InscriberResultSlotHandler(dataInventory, 6, 126, 57));
        this.addStandardInventorySlots(playerInventory, 8, 129);
	}

    @Override
    public void slotsChanged(Container container)
    {
        super.slotsChanged(container);
        access.execute((level, blockPos) -> {
            if(level instanceof ServerLevel sLevel)
            {
                SpellItem spellItem = (SpellItem) inscriberEntity.getItem(0).getItem();
                List<ItemStack> items = inscriberEntity.getItems().subList(1, inscriberEntity.getItems().size() - 1);
                inscriberInput = new SpellInscriberInput(spellItem, items);
                Optional<RecipeHolder<SpellInscriberRecipe>> optional = sLevel.recipeAccess().getRecipeFor(
                        AuguracyRecipes.INSCRIBER_RECIPE_TYPE.get(),
                        inscriberInput,
                        level
                );
                ItemStack result = optional.map(RecipeHolder::value).map(recipe ->
                        recipe.assemble(inscriberInput, level.registryAccess())).orElse(ItemStack.EMPTY);

                if(!result.isEmpty())
                {
                    getSlot(6).set(result);
                    setRemoteSlot(6, result);
                    ServerPlayer sPlayer = (ServerPlayer) player;
                    sPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), 6, result));
                }
            }
        });
    }

    private void RetrieveInscriberBlockEntity(ContainerLevelAccess access)
    {
        access.execute((level, blockPos) -> inscriberEntity = (SpellInscriberEntity) level.getBlockEntity(blockPos));
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

            //Quick moving from spell inscriber to player inv
            if(slotIndex <= 5)
            {
                if(!this.moveItemStackTo(slotItem, 7, 43, false))
                    return ItemStack.EMPTY;
            }
            //Quick moving from player inv or hotbar
            else if(slotIndex > 6 && slotIndex < 43)
            {
                //Try to quick move to inscriber input slots
                if(!this.moveItemStackTo(slotItem, 0, 6, false))
                {
                    //If can't move item to inscriber, quick move from player inv to hotbar
                    if(slotIndex < 34)
                    {
                        if(!this.moveItemStackTo(slotItem, 34, 43, false))
                            return ItemStack.EMPTY;
                    }
                    //Quick move from hotbar to player inv
                    else if (!this.moveItemStackTo(slotItem, 7, 34, false))
                        return ItemStack.EMPTY;
                }
            }
            //Quick moving from result slot to player inv or hotbar
            else if(this.moveItemStackTo(slotItem, 7, 43, true))
                quickMovedSlot.onQuickCraft(slotItem, quickMovedStack);
            else //Can't quick move stack
                return ItemStack.EMPTY;

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
        return AbstractContainerMenu.stillValid(this.access, player, AuguracyBlocks.SPELL_INSCRIBER.get());
    }

    private void validateInventorySize(IItemHandler slots)
    {
        if(slots.getSlots() < SpellInscriberMenu.INV_SIZE)
        {
            Auguracy.LOGGER.atError().log("Invalid inventory size for Spell Inscriber menu. \nExpected: " + SpellInscriberMenu.INV_SIZE + " Got: " + slots.getSlots());
        }
    }

    static class SpellItemSlotHandler extends SlotItemHandler
    {
        public SpellItemSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(AuguracyItems.SPELL_SCROLL) || stack.is(AuguracyItems.SPELL_CRYSTAL);
        }
    }

    static class InscriberResultSlotHandler extends SlotItemHandler
    {
        public InscriberResultSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
