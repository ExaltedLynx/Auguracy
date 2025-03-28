package net.exaltedlynx.auguracy.common.data_attachments.mana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.network.SyncManaPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class Mana
{
    private static final int BASE_MAX_MANA = 50;

    private int maxMana;
    private int currentMana;

    public static final Codec<Mana> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.INT.fieldOf("max_mana").forGetter(Mana::getMaxMana),
                Codec.INT.fieldOf("current_mana").forGetter(Mana::getCurrentMana)
        ).apply(inst, Mana::new)
    );

    public Mana()
    {
        maxMana = BASE_MAX_MANA;
        currentMana = maxMana;
    }

    public Mana(int maxMana, int currentMana)
    {
        this.maxMana = maxMana;
        this.currentMana = currentMana;
    }

    public void subtract(int amount, Player player)
    {
        currentMana -= amount;
        player.setData(AuguracyAttachments.MANA, this);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncManaPacket(this));
    }

    public void add(int amount, Player player)
    {
        currentMana += amount;
        player.setData(AuguracyAttachments.MANA, this);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncManaPacket(this));
    }

    public void addMax(int amount, Player player)
    {
        maxMana += amount;
        player.setData(AuguracyAttachments.MANA, this);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncManaPacket(this));
    }

    public int getMaxMana() { return maxMana; }

    public int getCurrentMana() { return currentMana; }
}
