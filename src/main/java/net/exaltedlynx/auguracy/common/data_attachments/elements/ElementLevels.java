package net.exaltedlynx.auguracy.common.data_attachments.elements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.network.SyncElementLevelsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.stream.IntStream;


public class ElementLevels
{
    public static final int MAX_LEVEL = 50;
    private static final int XP_LIMIT_BASE = 10;
    private static final int XP_LIMIT_MULT = 15;
    private static final int XP_LIMIT_SCALE = 2;

    private int[] levels;
    private int[] exp;

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
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncElementLevelsPacket(this));
        }
    }

    public void addExp(ElementType type, int amount, Player player)
    {
        exp[type.ordinal()] += amount;
        player.setData(AuguracyAttachments.ELEMENT_LEVELS, this);
        if(exp[type.ordinal()] >= getLevelExpLimit(getLevel(type)))
            incrementLevel(type, player);
        else
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncElementLevelsPacket(this));

    }

    private int getLevelExpLimit(int level)
    {
        return (int) (XP_LIMIT_BASE + Math.pow(Math.log10((level + 1)) * XP_LIMIT_MULT, XP_LIMIT_SCALE));
    }

    private IntStream getLevelsAsStream() { return Arrays.stream(levels); }
    private IntStream getExpAsStream() {return Arrays.stream(exp); }
}
