package net.exaltedlynx.auguracy.client.gui;

import net.exaltedlynx.auguracy.Auguracy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.spi.LoggingEventBuilder;

public class ElementsScreen extends Screen
{
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "textures/gui/elements_gui.png");
    private static final int SPRITE_WIDTH = 89;
    private static final int SPRITE_HEIGHT = 42;
    private int xPos;
    private int yPos;

    protected ElementsScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        xPos = (this.width - SPRITE_WIDTH) / 2;
        yPos = (this.height - SPRITE_HEIGHT) / 2;
        LoggingEventBuilder builder = Auguracy.LOGGER.atDebug();
        builder.log(xPos + ", " + yPos);
        builder.log();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        xPos = (this.width - SPRITE_WIDTH) / 2;
        yPos = (this.height - SPRITE_HEIGHT) / 2;
        graphics.blit(RenderType::guiTextured, TEXTURE, xPos, yPos, 0, 0, SPRITE_WIDTH, SPRITE_HEIGHT, 256, 256);
    }
}
