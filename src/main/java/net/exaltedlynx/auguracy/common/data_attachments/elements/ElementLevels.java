package net.exaltedlynx.auguracy.common.data_attachments.elements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.IntStream;


public class ElementLevels
{
    public static final int MAX_LEVEL = 50;
    private static final int XP_LIMIT_BASE = 10;
    private static final int XP_LIMIT_MULT = 20;
    private static final int XP_LIMIT_SCALE = 2;

    private final int[] levels;
    private final int[] exp;

    public static final Codec<ElementLevels> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT_STREAM.fieldOf("levels").forGetter(ElementLevels::getLevelsAsStream),
            Codec.INT_STREAM.fieldOf("exp").forGetter(ElementLevels::getExpAsStream)
            ).apply(inst, ElementLevels::new)
    );

    public ElementLevels()
    {
        levels = new int[] {0, 0, 0, 0, 0};
        exp = new int[] {0, 0, 0, 0, 0};
    }

    private ElementLevels(int[] levels, int[] exp) {
        this.levels = levels;
        this.exp = exp;
    }

    private ElementLevels(IntStream levelsStream, IntStream expStream) {
        levels = levelsStream.toArray();
        exp = expStream.toArray();
    }

    public int getLevel(ElementType type)
    {
        return levels[type.ordinal()];
    }

    public void incrementLevel(ElementType type, Player player)
    {
        if(levels[type.ordinal()] < MAX_LEVEL)
        {
            levels[type.ordinal()]++;
            player.setData(AuguracyAttachments.ELEMENT_LEVELS, this);
        }
    }

    private int getLevelExpLimit(int level)
    {
        return (int) (XP_LIMIT_BASE + Math.pow(Math.log10((level + 1) * XP_LIMIT_MULT), XP_LIMIT_SCALE));
    }

    public void addExp(ElementType type, int amount, Player player)
    {
        exp[type.ordinal()] += amount;
        player.setData(AuguracyAttachments.ELEMENT_LEVELS, this);

        ElementLevels elementLevels = player.getData(AuguracyAttachments.ELEMENT_LEVELS);
        if(exp[type.ordinal()] >= getLevelExpLimit(elementLevels.getLevel(type)))
        {
            elementLevels.incrementLevel(type, player);

        }
    }

    private IntStream getLevelsAsStream() { return Arrays.stream(levels); }
    private IntStream getExpAsStream() {return Arrays.stream(exp); }

    public static IAttachmentSerializer<CompoundTag, ElementLevels> getSerializer()
    {
        return new IAttachmentSerializer<>() {
            @Override
            public ElementLevels read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
                return new ElementLevels(tag.getIntArray("elem_lvls"), tag.getIntArray("elem_exp"));
            }

            @Override
            public @Nullable CompoundTag write(ElementLevels attachment, HolderLookup.Provider provider) {
                CompoundTag tag = new CompoundTag();
                tag.putIntArray("elem_lvls", attachment.levels);
                tag.putIntArray("elem_exp", attachment.exp);
                return tag;
            }
        };
    }
}
