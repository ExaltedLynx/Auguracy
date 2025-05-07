package net.exaltedlynx.auguracy.common.events;

import net.exaltedlynx.auguracy.common.datagen.AuguracyTagsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class PlayerEventHandler
{
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		if(!event.getEntity().isCrouching() && event.getItemStack().is(AuguracyTagsProvider.Items.SPELL_CASTER_ITEM_TAG))
			event.setUseBlock(TriState.FALSE);
	}
}
