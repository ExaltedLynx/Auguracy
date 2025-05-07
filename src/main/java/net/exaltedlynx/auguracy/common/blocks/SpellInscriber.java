package net.exaltedlynx.auguracy.common.blocks;

import com.mojang.serialization.MapCodec;
import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.blocks.blockentities.SpellInscriberEntity;
import net.exaltedlynx.auguracy.setup.AuguracyBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SpellInscriber extends Block implements EntityBlock
{
    private static final MapCodec<SpellInscriber> CODEC = simpleCodec(SpellInscriber::new);

    public SpellInscriber(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpellInscriberEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        Auguracy.LOGGER.atDebug().log("reached menu");
        if(!level.isClientSide && player instanceof ServerPlayer sPlayer)
            sPlayer.openMenu(state.getMenuProvider(level, pos));

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
    {
        if(state.getBlock() != newState.getBlock())
        {
            level.getBlockEntity(pos, AuguracyBlocks.SPELL_INSCRIBER_ENTITY.get()).ifPresent(inscriber -> {
                inscriber.dropItems(level, pos);
                super.onRemove(state, level, pos, newState, movedByPiston);
            });
        }
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
