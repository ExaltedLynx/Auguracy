package net.exaltedlynx.auguracy.client.gui.menu;

import net.exaltedlynx.auguracy.setup.AuguracyBlocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SpellInscriberMenu extends AbstractCraftingMenu
{
    private final ContainerLevelAccess access;

    public SpellInscriberMenu(MenuType<?> menuType, int containerId, int width, int height) {
        this(menuType, containerId, width, height, ContainerLevelAccess.NULL);
    }

    public SpellInscriberMenu(MenuType<?> menuType, int containerId, int width, int height, ContainerLevelAccess access) {
        super(menuType, containerId, width, height);
        this.access = access;
    }

    @Override
    public Slot getResultSlot() {
        return null;
    }

    @Override
    public List<Slot> getInputGridSlots() {
        return List.of();
    }

    @Override
    protected Player owner() {
        return null;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return null;
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
