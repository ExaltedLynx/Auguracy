package net.exaltedlynx.auguracy.common.blocks.blockentities;

import net.exaltedlynx.auguracy.common.containers.SpellInscriberMenu;
import net.exaltedlynx.auguracy.setup.AuguracyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

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

    private ItemStackHandler getInventory()
    {
        return new ItemStackHandler(items);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return SpellInscriberMenu.createServerMenu(containerId, inventory, getInventory(), ContainerLevelAccess.create(level, getBlockPos()));
    }

    @Override
    public int getContainerSize() {
        return INV_SIZE;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, this.items, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        this.items = NonNullList.withSize(INV_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, provider);
    }
}
