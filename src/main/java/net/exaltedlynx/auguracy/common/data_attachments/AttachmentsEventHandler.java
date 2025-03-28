package net.exaltedlynx.auguracy.common.data_attachments;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementLevels;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.common.data_attachments.mana.Mana;
import net.exaltedlynx.auguracy.common.network.SyncElementLevelsPacket;
import net.exaltedlynx.auguracy.common.network.SyncManaPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Auguracy.MODID)
public class AttachmentsEventHandler
{
    @SubscribeEvent
    public static void onPlayerSpawn(PlayerEvent.PlayerLoggedInEvent event)
    {
        //Attaches new instance of attachment on first login of the player
        Player player = event.getEntity();
        if(!player.level().isClientSide())
        {
            ElementLevels levels = player.getData(AuguracyAttachments.ELEMENT_LEVELS);
            Mana mana = player.getData(AuguracyAttachments.MANA);
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncElementLevelsPacket(levels));
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncManaPacket(mana));
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeGameMode(PlayerEvent.PlayerChangeGameModeEvent event)
    {
        Player player = event.getEntity();
        if(!player.level().isClientSide() && event.getNewGameMode().isCreative())
        {
            player.setData(AuguracyAttachments.ELEMENT_LEVELS, new ElementLevels());
            ElementLevels levels = player.getData(AuguracyAttachments.ELEMENT_LEVELS);
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncElementLevelsPacket(levels));
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event)
    {
        Player player = event.getPlayer();
        if(!player.level().isClientSide())
        {
            if(event.getState().is(BlockTags.DIRT))
            {
                ElementLevels levels = player.getData(AuguracyAttachments.ELEMENT_LEVELS);
                levels.addExp(ElementType.EARTH, 1, player);
            }
        }
    }
}
