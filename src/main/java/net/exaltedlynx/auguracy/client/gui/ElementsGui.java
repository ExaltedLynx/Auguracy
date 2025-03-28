package net.exaltedlynx.auguracy.client.gui;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementLevels;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ElementsGui
{
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "textures/gui/elements_gui.png");
    private static final int SPRITE_WIDTH = 89;
    private static final int SPRITE_HEIGHT = 42;
    private static final int OFFSET_X = -1;
    private static final int OFFSET_Y = -250;
    Minecraft minecraft = Minecraft.getInstance();

    public ElementsGui() { }

    public void renderElementLevels(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int xPos = (minecraft.getWindow().getGuiScaledWidth() + OFFSET_X) / 2;
        int yPos = (minecraft.getWindow().getGuiScaledHeight() + OFFSET_Y) / 2;
        graphics.blit(RenderType::guiTextured, TEXTURE, xPos, yPos, 0, 0, SPRITE_WIDTH, SPRITE_HEIGHT, 256, 256);

        ElementLevels elementLevels = minecraft.player.getData(AuguracyAttachments.ELEMENT_LEVELS);
        int offset = 24;
        for(int i = 0; i < 5; i++)
        {
            int level = elementLevels.getLevel(ElementType.fromInt(i));
            renderLvlNumber(graphics, offset, String.valueOf(level));
            offset += 32;
        }
    }

    private void renderLvlNumber(GuiGraphics graphics, int offset, String level)
    {
        Font font = minecraft.font;
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int width = (screenWidth + offset - font.width(level)) / 2;
        int height = screenHeight - 268;

        graphics.drawString(font, level, width + 1, height, 0, false);
        graphics.drawString(font, level, width - 1, height, 0, false);
        graphics.drawString(font, level, width, height + 1, 0, false);
        graphics.drawString(font, level, width, height - 1, 0, false);
        graphics.drawString(font, level, width, height, 16777215, false);
    }
}
