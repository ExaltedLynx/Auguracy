package net.exaltedlynx.auguracy.client.gui.hud;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.containers.WandMenu;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpellSelector
{
	public static final ResourceLocation SPELL_SELECTOR = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "spell_selector");
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "textures/gui/wand_inv_hud.png");
	private static final int HUD_WIDTH = 62; private static final int HUD_HEIGHT = 22;
	private static final int SELECTOR_WIDTH = 24; private static final int SELECTOR_HEIGHT = 24;
	private static final int OFFSET_X = -90; private static final int OFFSET_Y = -35;
	private static final Minecraft minecraft = Minecraft.getInstance();

	public static void renderSpellSelector(GuiGraphics graphics, DeltaTracker deltaTracker)
	{
		Player player = minecraft.player;
		ItemStack stack = player.getItemInHand(player.getUsedItemHand());
		if(!stack.isEmpty() && stack.is(AuguracyItems.DIVINE_WAND))
		{
			int xPos = minecraft.getWindow().getGuiScaledWidth() + OFFSET_X - HUD_WIDTH;
			int yPos = minecraft.getWindow().getGuiScaledHeight() + OFFSET_Y - HUD_HEIGHT;
			//render hud
			graphics.blit(RenderType::guiTextured, TEXTURE, xPos, yPos, 0, 0, HUD_WIDTH, HUD_HEIGHT, 256, 256);
			//render items inside wand on top

			//render selector (defaults to first slot)
			int selectedSpellSlot = WandMenu.getSelectedSlot();
			int selectorOffset = 20 * selectedSpellSlot;
			graphics.blit(RenderType::guiTextured, TEXTURE, xPos + selectorOffset - 1, yPos - 1, 0, 23, SELECTOR_WIDTH, SELECTOR_HEIGHT, 256, 256);
		}
	}

}
