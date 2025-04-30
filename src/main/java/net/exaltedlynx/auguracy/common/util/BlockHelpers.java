package net.exaltedlynx.auguracy.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlockHelpers
{
	//same as Block.playerDestroy except with no hunger exhaustion
	public static void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool)
	{
		player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
		Block.dropResources(state, level, pos, blockEntity, player, tool);
	}

	public static boolean isItemProperToolForBlock(ItemStack stack, BlockState state)
	{
		return !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
	}

	public static boolean canBreak(BlockState state, float blockHardness)
	{
		return !(state.isAir() || blockHardness == -1);
	}
}
