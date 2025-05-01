package net.exaltedlynx.auguracy.client.gui.hud;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpellSelector
{
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "textures/gui/wand_inv_hud.png");
	private static final int HUD_WIDTH = 59; private static final int HUD_HEIGHT = 19;
	private static final int SELECTOR_WIDTH = 22; private static final int SELECTOR_HEIGHT = 22;
	private static final Minecraft minecraft = Minecraft.getInstance();

	public static void renderSpellSelector(GuiGraphics graphics, DeltaTracker deltaTracker)
	{
		Player player = minecraft.player;
		ItemStack stack = player.getItemInHand(player.getUsedItemHand());
		if(!stack.isEmpty() && stack.is(AuguracyItems.DIVINE_WAND))
		{
			//render hud
			//render items inside wand on top
			//render selector (defaults to first slot)
		}
	}

}
