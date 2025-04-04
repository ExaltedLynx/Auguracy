package net.exaltedlynx.auguracy.common.spell;

import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;

public class Spells
{
    public static class DigSpell extends Spell implements ICorruptable
    {
        private Tool toolComponent;
        private double range = 4.5;

        public DigSpell(String name, ElementType type, int lvlReq, int manaCost)
        {
            this.name = name;
            this.type = type;
            this.lvlReq = lvlReq;
            this.manaCost = manaCost;
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
            if(blockHitResult.getType() == HitResult.Type.BLOCK)
            {
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = level.getBlockState(blockPos);
                ServerPlayer sPlayer = (ServerPlayer) caster;
                //var event = CommonHooks.fireBlockBreak(caster.level(), sPlayer.gameMode.getGameModeForPlayer(), sPlayer, blockPos, blockState);
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
    }
}
