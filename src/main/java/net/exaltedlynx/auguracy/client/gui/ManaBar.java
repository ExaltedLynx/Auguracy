package net.exaltedlynx.auguracy.client.gui;

import net.exaltedlynx.auguracy.Auguracy;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
public class ManaBar
{
    public static final ResourceLocation MANA_BAR = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "manabar");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "textures/gui/mana_had.png");
    private static final int OFFSET_X = -150;
    private static final int OFFSET_Y = -250;
    private static Minecraft minecraft = Minecraft.getInstance();

    public static void renderManaBar(GuiGraphics graphics, DeltaTracker deltaTracker)
    {
        int xPos = (minecraft.getWindow().getGuiScaledWidth() + OFFSET_X) / 2;
        int yPos = (minecraft.getWindow().getGuiScaledHeight() + OFFSET_Y) / 2;
        graphics.blit(RenderType::guiTextured, TEXTURE, xPos, yPos, 0, 0, 71, 17, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, xPos + 100, yPos + 100,  18, 0, 67, 13, 256, 256);
    }
}
