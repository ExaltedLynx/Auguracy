package net.exaltedlynx.auguracy.client.gui.screens;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.containers.SpellInscriberMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SpellInscriberScreen extends AbstractContainerScreen<SpellInscriberMenu>
{
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "textures/gui/inscriber_gui.png");
	private static final int SCREEN_WIDTH = 175;
	private static final int SCREEN_HEIGHT = 210;
	private static final int INSRIBER_LABEL_X = 5;
	private static final int INSRIBER_LABEL_Y = 5;
	private static final int INV_LABEL_X = 8;
	private static final int INV_LABEL_Y = 118;
	private static final Component LABEL = Component.translatable("screen.auguracy.inscriber_screen");

	public SpellInscriberScreen(SpellInscriberMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
		this.imageWidth = SCREEN_WIDTH;
		this.imageHeight = SCREEN_HEIGHT;
		this.titleLabelX = INSRIBER_LABEL_X;
		this.titleLabelY = INSRIBER_LABEL_Y;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(graphics, mouseX, mouseY, partialTick);
		super.render(graphics, mouseX, mouseY, partialTick);
		this.renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		graphics.blit(RenderType::guiTextured, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY)
	{
		graphics.drawString(this.font, LABEL, titleLabelX, titleLabelY, 0x404040, false);
		graphics.drawString(font, Component.translatable("container.inventory"), INV_LABEL_X, INV_LABEL_Y, 0xFF404040, false);
	}
}
