package net.exaltedlynx.auguracy.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

public class Spells
{
    public static class DigSpell extends Spell implements ICorruptable
    {
        private Tool toolComponent;
        private double range = 4.5;
        private int destroyProgress;
        private int ticksUntilNextProgress;

        private static final MapCodec<DigSpell> CODEC = RecordCodecBuilder.mapCodec(inst -> Spell.startSpellCodec(inst).and(
                inst.group(
                        Tool.CODEC.fieldOf("tool").forGetter(DigSpell::getTool),
                        Codec.DOUBLE.fieldOf("range").forGetter(DigSpell::getRange),
                        Codec.INT.fieldOf("destroy_progress").forGetter(DigSpell::getDestroyProgress),
                        Codec.INT.fieldOf("tunp").forGetter(DigSpell::getTicksUntilNextProgress)
                )).apply(inst, DigSpell::initDigSpell));

        public DigSpell(String name, ElementType type, int lvlReq, int manaCost)
        {
            this.name = name;
            this.type = type;
            this.lvlReq = lvlReq;
            this.manaCost = manaCost;
        }

        public static DigSpell initDigSpell(String name, Tool tool, double range, int destroyProgress, int ticksUntilNextProgress)
        {
            DigSpell spell = (DigSpell) AuguracySpells.getSpellFromName(name);
            spell.toolComponent = tool;
            spell.range = range;
            spell.destroyProgress = destroyProgress;
            spell.ticksUntilNextProgress = ticksUntilNextProgress;
            return spell;
        }

        @Override
        protected boolean onCast(Player caster) {

            Level level = caster.level();
            Vec3 playerEyePos = caster.getEyePosition();
            Vec3 viewDirection = playerEyePos.add(caster.calculateViewVector(caster.getXRot(), caster.getYRot()).scale(range));

            //Raycast
            BlockHitResult blockHitResult = level.clip(new ClipContext(playerEyePos, viewDirection,
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, caster));
            caster.displayClientMessage(Component.literal(blockHitResult.getType().toString()), false);
            if (blockHitResult.getType() == HitResult.Type.BLOCK)
            {
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = level.getBlockState(blockPos);
                float blockHardness = blockState.getDestroySpeed(level, blockPos);
                float breakSpeed = toolComponent.getMiningSpeed(blockState);
                ServerPlayer sPlayer = (ServerPlayer) caster;

                if (ticksUntilNextProgress < 0)
                    return false;
                if (ticksUntilNextProgress-- > 0)
                    return false;

                var event = CommonHooks.fireBlockBreak(caster.level(), sPlayer.gameMode.getGameModeForPlayer(), sPlayer, blockPos, blockState);
                if (event.isCanceled())
                    return false;

                //credit to Create mod
                destroyProgress += Mth.clamp((int) (breakSpeed / blockHardness), 1, 10 - destroyProgress);
                level.playSound(sPlayer, blockPos, blockState.getSoundType(level, blockPos, sPlayer).getHitSound(), SoundSource.BLOCKS);

                if (destroyProgress >= 10) {
                    level.destroyBlock(blockPos, true, sPlayer);
                    destroyProgress = 0;
                    ticksUntilNextProgress = -1;
                    level.destroyBlockProgress(sPlayer.getId(), blockPos, -1);
                    return true;
                }

                ticksUntilNextProgress = (int) (blockHardness / breakSpeed);
                level.destroyBlockProgress(sPlayer.getId(), blockPos, destroyProgress);
            }
            return false;
        }

        @Override
        public boolean corruptedCast(Player caster) {
            return false;
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

        @Override
        public MapCodec<? extends Spell> getCodec() {
            return CODEC;
        }
    }
}
