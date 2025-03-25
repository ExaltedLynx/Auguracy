package net.exaltedlynx.auguracy.common.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManaFlower extends FlowerBlock
{
    public static final MapCodec<ManaFlower> CODEC = RecordCodecBuilder.mapCodec(
            codec -> codec.group(EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects), propertiesCodec()).apply(codec, ManaFlower::new)
    );
    public static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    public ManaFlower(Holder<MobEffect> effect, float seconds, Properties properties) {
        this(makeEffectList(effect, seconds), properties);
    }

    public ManaFlower(SuspiciousStewEffects stewEffect, Properties properties) {
        super(stewEffect, properties);
    }

    @Override
    public MapCodec<ManaFlower> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
    }
}
