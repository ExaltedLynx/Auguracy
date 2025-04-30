package net.exaltedlynx.auguracy.common.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHelpers
{
	public static boolean isItemProperToolForBlock(ItemStack stack, BlockState state)
	{
		return !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
	}

	public static boolean canBreak(BlockState state, float blockHardness)
	{
		return !(state.isAir() || blockHardness == -1);
	}

}
