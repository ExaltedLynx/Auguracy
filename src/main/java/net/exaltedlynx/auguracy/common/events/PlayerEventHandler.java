package net.exaltedlynx.auguracy.common.events;

import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class PlayerEventHandler
{
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getItemStack().has(AuguracyDataComponents.SPELL_CONTAINER))
		{
			event.setCanceled(true);
		}
	}
}
