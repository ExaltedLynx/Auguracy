package net.exaltedlynx.auguracy.client.gui.screens;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.client.gui.menus.SpellInscriberMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SpellInscriberScreen extends AbstractContainerScreen<SpellInscriberMenu>
{
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "textures/gui/inscriber_gui.png");
	private static final Component LABEL = Component.translatable("screen.auguracy.inscriber_screen");

	public SpellInscriberScreen(SpellInscriberMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
		this.titleLabelX = 10;
		this.titleLabelY = 10;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		graphics.blit(RenderType::guiTextured, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageWidth, 256, 256);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY)
	{
		super.renderLabels(graphics, mouseX, mouseY);
		graphics.drawString(this.font, LABEL, titleLabelX, titleLabelY, 0x404040, false);
	}
}
