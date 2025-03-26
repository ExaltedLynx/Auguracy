package net.exaltedlynx.auguracy.common.data_attachments;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementLevels;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Auguracy.MODID)
public class AttachmentsEventHandler
{
    @SubscribeEvent
    public static void onPlayerSpawn(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getEntity();
        if(!player.level().isClientSide() && !player.hasData(AuguracyAttachments.ELEMENT_LEVELS))
        {
            //Attaches new instance on first login of the player
            player.getData(AuguracyAttachments.ELEMENT_LEVELS);
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
                ElementLevels elemLvls = player.getData(AuguracyAttachments.ELEMENT_LEVELS);
                elemLvls.addExp(ElementType.EARTH, 1, player);
            }
        }
    }
}
