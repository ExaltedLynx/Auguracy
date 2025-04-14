package net.exaltedlynx.auguracy.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.checkerframework.checker.units.qual.A;

public class Spells
{
    public static class DigSpell extends Spell implements ICorruptable
    {
        private Tool toolComponent;
        private double range = 4.5;
        private int destroyProgress;
        private int ticksUntilNextProgress;
        private BlockPos currentBlock = BlockPos.ZERO;

        public static final MapCodec<DigSpell> CODEC = RecordCodecBuilder.mapCodec(inst -> Spell.startSpellCodec(inst).and(
                inst.group(
                        Tool.CODEC.fieldOf("tool").forGetter(DigSpell::getTool),
                        Codec.DOUBLE.fieldOf("range").forGetter(DigSpell::getRange),
                        Codec.INT.fieldOf("destroy_progress").forGetter(DigSpell::getDestroyProgress),
                        Codec.INT.fieldOf("tunp").forGetter(DigSpell::getTicksUntilNextProgress),
                        BlockPos.CODEC.fieldOf("currentBlock").forGetter(DigSpell::getCurrentBlock)
                )).apply(inst, DigSpell::initDigSpell));

        public DigSpell(String name, ElementType type, int lvlReq, int manaCost)
        {
            this.name = name;
            this.type = type;
            this.lvlReq = lvlReq;
            this.manaCost = manaCost;
            setToolComponent((PickaxeItem) Items.WOODEN_PICKAXE);
        }

        public static DigSpell initDigSpell(String name, Tool tool, double range, int destroyProgress, int ticksUntilNextProgress, BlockPos currentBlock)
        {
            DigSpell spell = (DigSpell) AuguracySpells.getSpellFromName(name);
            spell.toolComponent = tool;
            spell.range = range;
            spell.destroyProgress = destroyProgress;
            spell.ticksUntilNextProgress = ticksUntilNextProgress;
            spell.currentBlock = currentBlock;
            return spell;
        }

        @Override
        protected boolean onCast(Player caster) {

            Level level = caster.level();
            Vec3 playerEyePos = caster.getEyePosition();
            Vec3 viewDirection = playerEyePos.add(caster.calculateViewVector(caster.getXRot(), caster.getYRot()).scale(range));
            ServerPlayer sPlayer = (ServerPlayer) caster;

            //Raycast
            BlockHitResult blockHitResult = level.clip(new ClipContext(playerEyePos, viewDirection,
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, caster));

            if (blockHitResult.getType() == HitResult.Type.BLOCK)
            {
                if(!blockHitResult.getBlockPos().equals(currentBlock))
                {
                    resetBlockDestroyProgress(level, sPlayer);
                }
                currentBlock = blockHitResult.getBlockPos().immutable();
                BlockState blockState = level.getBlockState(currentBlock);
                return handleBlockMining(level, sPlayer, blockState, blockHitResult);
            }
            else if(currentBlock != BlockPos.ZERO)
            {
                resetBlockDestroyProgress(level, sPlayer);
            }
            return false;
        }

        @Override
        public boolean corruptedCast(Player caster) {
            return false;
        }

        @Override
        public void onCastRelease(Player caster) {
            Auguracy.LOGGER.atDebug().log(toolComponent.rules().toString());
            resetBlockDestroyProgress(caster.level(), (ServerPlayer) caster);
        }

        private boolean handleBlockMining(Level level, ServerPlayer sPlayer, BlockState blockState, BlockHitResult blockHitResult)
        {
            float blockHardness = blockState.getDestroySpeed(level, currentBlock);
            float breakSpeed = toolComponent.getMiningSpeed(blockState);

            //caster.displayClientMessage(Component.literal(String.valueOf(ticksUntilNextProgress)), false);

            //credit to Create mod
            if (ticksUntilNextProgress < 0)
                return false;
            if (ticksUntilNextProgress-- > 0)
                return false;

            var event = CommonHooks.fireBlockBreak(level, sPlayer.gameMode.getGameModeForPlayer(), sPlayer, currentBlock, blockState);
            if (event.isCanceled())
            {
                resetBlockDestroyProgress(level, sPlayer);
                return false;
            }

            destroyProgress += Mth.clamp((int) (breakSpeed / blockHardness), 1, 10 - destroyProgress);
            //cLevel.playSound(sPlayer, currentBlock, blockState.getSoundType(level, currentBlock, sPlayer).getHitSound(), SoundSource.BLOCKS);
            ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
            particleEngine.addBlockHitEffects(currentBlock, blockHitResult);

            if (destroyProgress >= 10) {
                level.destroyBlock(currentBlock, true, sPlayer);
                resetBlockDestroyProgress(level, sPlayer);
                return true;
            }

            ticksUntilNextProgress = (int) (blockHardness / breakSpeed);
            level.destroyBlockProgress(sPlayer.getId(), currentBlock, destroyProgress);
            sPlayer.connection.send(new ClientboundBlockDestructionPacket(sPlayer.getId(), currentBlock, destroyProgress));
            return false;
        }

        private void resetBlockDestroyProgress(Level level, ServerPlayer sPlayer)
        {
            destroyProgress = 0;
            ticksUntilNextProgress = 1;
            level.destroyBlockProgress(sPlayer.getId(), currentBlock, -1);
            sPlayer.connection.send(new ClientboundBlockDestructionPacket(sPlayer.getId(), currentBlock, -1));
        }

        public void setToolComponent(PickaxeItem pickaxe)
        {
            toolComponent = pickaxe.components().get(DataComponents.TOOL);
        }

        public Tool getTool()
        {
            return toolComponent;
        }

        public double getRange()
        {
            return range;
        }

        public int getDestroyProgress()
        {
            return destroyProgress;
        }

        public int getTicksUntilNextProgress()
        {
            return ticksUntilNextProgress;
        }

        public BlockPos getCurrentBlock()
        {
            return currentBlock;
        }

        @Override
        protected MapCodec<? extends Spell> getCodec() {
            return CODEC;
        }
    }
}
