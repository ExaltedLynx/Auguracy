package net.exaltedlynx.auguracy.common.blocks.blockentities;

import net.exaltedlynx.auguracy.common.containers.SpellInscriberMenu;
import net.exaltedlynx.auguracy.setup.AuguracyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Random;

public class SpellInscriberEntity extends BaseContainerBlockEntity
{
    public static final int INV_SIZE = 7;
    private NonNullList<ItemStack> items = NonNullList.withSize(INV_SIZE, ItemStack.EMPTY);

    public SpellInscriberEntity(BlockPos pos, BlockState blockState) {
        super(AuguracyBlocks.SPELL_INSCRIBER_ENTITY.get(), pos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.auguracy.inscriber_entity");
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    private final IItemHandler inventory = new ItemStackHandler(items)
    {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public void dropItems(Level level, BlockPos blockPos)
    {
        Random rand = new Random();
        IItemHandler inv = inventory;
        for (int i = 0; i < getItems().size() - 1; i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                double offsetX = rand.nextDouble(0.1, 0.7);
                double offsetY = rand.nextDouble(0.1, 0.7);
                double offsetZ = rand.nextDouble(0.1, 0.7);
                ItemEntity itemEntity = new ItemEntity(level, blockPos.getX() + offsetX, blockPos.getY() + offsetY, blockPos.getZ() + offsetZ, stack.copy());
                level.addFreshEntity(itemEntity);
            }
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInv) {
        return SpellInscriberMenu.createServerMenu(containerId, playerInv, inventory, ContainerLevelAccess.create(level, getBlockPos()));
    }

    @Override
    public int getContainerSize() {
        return INV_SIZE;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, items, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        //items = NonNullList.withSize(INV_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, provider);
    }
}
