package net.exaltedlynx.auguracy.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.common.util.BlockHelpers;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.Objects;
import java.util.function.Consumer;

public class Spells
{
    public static class DigSpell extends Spell implements ICorruptable
    {
        private ItemStack pickaxe;
        private double range = 4.5;
        private float destroyProgress;
        private int ticksUntilNextProgress;
        private BlockPos currentBlock = BlockPos.ZERO;

        public static final MapCodec<DigSpell> CODEC = RecordCodecBuilder.mapCodec(inst -> Spell.startSpellCodec(inst).and(
                inst.group(
                    ItemStack.SINGLE_ITEM_CODEC.fieldOf("pickaxe").forGetter(digSpell -> digSpell.pickaxe),
                    Codec.DOUBLE.fieldOf("range").forGetter(DigSpell::getRange),
                    Codec.FLOAT.fieldOf("destroy_progress").forGetter(DigSpell::getDestroyProgress),
                    Codec.INT.fieldOf("tunp").forGetter(DigSpell::getTicksUntilNextProgress),
                    BlockPos.CODEC.fieldOf("currentBlock").forGetter(DigSpell::getCurrentBlock)
                )).apply(inst, DigSpell::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, DigSpell> STREAM_CODEC = createStreamCodec(DigSpell::toBuffer, DigSpell::fromBuffer);
        public static final SpellType TYPE = new SpellType(CODEC, STREAM_CODEC);

        public DigSpell(String name, ElementType type, int lvlReq, int manaCost)
        {
            super(name, type, lvlReq, manaCost);
        }

        public DigSpell(String name, ItemStack pickaxe, double range, float destroyProgress, int ticksUntilNextProgress, BlockPos currentBlock)
        {
            super(name);
            this.pickaxe = pickaxe;
            this.range = range;
            this.destroyProgress = destroyProgress;
            this.ticksUntilNextProgress = ticksUntilNextProgress;
            this.currentBlock = currentBlock;
        }

        @Override
        protected boolean onCast(Player caster)
        {
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
            resetBlockDestroyProgress(caster.level(), (ServerPlayer) caster);
        }

        //credit to the Create team
        private boolean handleBlockMining(Level level, ServerPlayer sPlayer, BlockState blockState, BlockHitResult blockHitResult)
        {
            if (ticksUntilNextProgress < 0)
                return false;
            if (ticksUntilNextProgress-- > 0)
                return false;

            float blockHardness = blockState.getDestroySpeed(level, currentBlock);
            float breakSpeed = pickaxe.getDestroySpeed(blockState);
            //Auguracy.LOGGER.atDebug().log("Break Speed");
            //Auguracy.LOGGER.atDebug().log("Block: " + blockHardness);
            //Auguracy.LOGGER.atDebug().log("Tool: " + breakSpeed);

            if(!BlockHelpers.canBreak(blockState, blockHardness))
            {
                if(destroyProgress != 0)
                    resetBlockDestroyProgress(level, sPlayer);
            }

            var event = CommonHooks.fireBlockBreak(level, sPlayer.gameMode.getGameModeForPlayer(), sPlayer, currentBlock, blockState);
            if (event.isCanceled()) {
                resetBlockDestroyProgress(level, sPlayer);
                return false;
            }

            boolean canToolMineBlock = BlockHelpers.isItemProperToolForBlock(pickaxe, blockState);
            Auguracy.LOGGER.atDebug().log(String.valueOf(canToolMineBlock));
            int i = canToolMineBlock ? 30 : 100;
            destroyProgress += (breakSpeed / blockHardness / i) * 10.0f;
            int destroyStage = Mth.floor(destroyProgress);
            destroyStage = Mth.clamp(destroyStage, 0, 10);

            //Auguracy.LOGGER.atDebug().log("Destroy Progress");
            //Auguracy.LOGGER.atDebug().log(String.valueOf((breakSpeed / blockHardness / i)));
            //Auguracy.LOGGER.atDebug().log(String.valueOf(destroyProgress));
            //Auguracy.LOGGER.atDebug().log(String.valueOf(destroyStage));

            //cLevel.playSound(sPlayer, currentBlock, blockState.getSoundType(level, currentBlock, sPlayer).getHitSound(), SoundSource.BLOCKS);
            ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
            particleEngine.addBlockHitEffects(currentBlock, blockHitResult);

            if (destroyProgress >= 10) {
                blockState.getBlock().playerWillDestroy(level, currentBlock, blockState, sPlayer);
                BlockHelpers.playerDestroy(level, sPlayer, currentBlock, blockState, null, pickaxe);
                level.destroyBlock(currentBlock, false, sPlayer);
                resetBlockDestroyProgress(level, sPlayer);
                ticksUntilNextProgress = 6;
                return true;
            }

            ticksUntilNextProgress = (int) (breakSpeed / blockHardness / i);

            level.destroyBlockProgress(sPlayer.getId(), currentBlock, destroyStage);
            sPlayer.connection.send(new ClientboundBlockDestructionPacket(sPlayer.getId(), currentBlock, destroyStage));
            return false;
        }

        private void resetBlockDestroyProgress(Level level, ServerPlayer sPlayer)
        {
            destroyProgress = 0;
            ticksUntilNextProgress = 1;
            level.destroyBlockProgress(sPlayer.getId(), currentBlock, -1);
            sPlayer.connection.send(new ClientboundBlockDestructionPacket(sPlayer.getId(), currentBlock, -1));
        }

        public static void toBuffer(RegistryFriendlyByteBuf buffer, DigSpell spell) {
            ItemStack.STREAM_CODEC.encode(buffer, spell.pickaxe);
            buffer.writeDouble(spell.range);
            buffer.writeFloat(spell.destroyProgress);
            buffer.writeVarInt(spell.ticksUntilNextProgress);
            BlockPos.STREAM_CODEC.encode(buffer, spell.currentBlock);
        }

        public static DigSpell fromBuffer(RegistryFriendlyByteBuf buffer) {
            DigSpell spell = AuguracySpells.DIG.get();
            spell.pickaxe = ItemStack.STREAM_CODEC.decode(buffer);
            spell.range = buffer.readDouble();
            spell.destroyProgress = buffer.readFloat();
            spell.ticksUntilNextProgress = buffer.readVarInt();
            spell.currentBlock = BlockPos.STREAM_CODEC.decode(buffer);
            return spell;
        }

        public void setPickaxe(ItemStack pickaxe)
        {
            this.pickaxe = pickaxe;
        }

        public ItemStack getPickaxe()
        {
            return this.pickaxe;
        }

        public double getRange()
        {
            return range;
        }

        public float getDestroyProgress()
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
        public void addTooltipInfo(Consumer<Component> tooltipAdder) {
            super.addTooltipInfo(tooltipAdder);
            tooltipAdder.accept(Component.translatable("spell.auguracy.dig_spell.pickaxe").append(pickaxe.getItemName()));
        }

        @Override
        public Spell newSpellInstance() {
            return new DigSpell(name, pickaxe, range, destroyProgress, ticksUntilNextProgress, currentBlock);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DigSpell digSpell)) return false;
            if (!super.equals(o)) return false;
            return Double.compare(range, digSpell.range) == 0 && destroyProgress == digSpell.destroyProgress && ticksUntilNextProgress == digSpell.ticksUntilNextProgress && Objects.equals(pickaxe, digSpell.pickaxe) && Objects.equals(currentBlock, digSpell.currentBlock);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), pickaxe, range, destroyProgress, ticksUntilNextProgress, currentBlock);
        }

        @Override
        protected SpellType spellType() {
            return TYPE;
        }
    }

    public static class EmptySpell extends Spell
    {
        public static final MapCodec<EmptySpell> CODEC = createSimpleCodec(EmptySpell::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, EmptySpell> STREAM_CODEC = createSimpleStreamCodec(EmptySpell::new);
        public static final SpellType TYPE = new SpellType(CODEC, STREAM_CODEC);

        public EmptySpell(String name, ElementType type, int lvlReq, int manaCost)
        {
            super(name, type, lvlReq, manaCost);
        }

        public EmptySpell(String name)
        {
            super(name);
        }

        @Override
        protected boolean onCast(Player caster) {
            caster.displayClientMessage(Component.literal("This is contains empty spell: Someone made an oopsie"), false);
            return true;
        }

        @Override
        public Spell newSpellInstance() {
            return new EmptySpell(name, type, lvlReq, manaCost);
        }

        @Override
        protected SpellType spellType() {
            return TYPE;
        }
    }
}
