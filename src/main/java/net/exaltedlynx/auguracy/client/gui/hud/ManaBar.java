package net.exaltedlynx.auguracy.client.gui.hud;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.data_attachments.mana.Mana;
import net.exaltedlynx.auguracy.common.datagen.AuguracyTagsProvider;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ManaBar
{
    public static final ResourceLocation MANA_BAR = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "manabar");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "textures/gui/mana_hud.png");
    private static final int BORDER_WIDTH = 71; private static final int BORDER_HEIGHT = 17;
    private static final int MANA_BAR_WIDTH = 67; private static final int MANA_BAR_HEIGHT = 13;
    private static final int OFFSET_X = -85;
    private static final int OFFSET_Y = -10;
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void renderManaBar(GuiGraphics graphics, DeltaTracker deltaTracker)
    {
        Player player = minecraft.player;
        ItemStack stack = player.getItemInHand(player.getUsedItemHand());
        if(!stack.isEmpty() && stack.is(AuguracyTagsProvider.Items.SPELL_CASTER_ITEM_TAG))
        {
            int xPos = minecraft.getWindow().getGuiScaledWidth() + OFFSET_X - BORDER_WIDTH;
            int yPos = minecraft.getWindow().getGuiScaledHeight() + OFFSET_Y - BORDER_HEIGHT;

            Mana mana = minecraft.player.getData(AuguracyAttachments.MANA);
            int currWidth = MANA_BAR_WIDTH;
            currWidth *= ((float) mana.getCurrentMana() / mana.getMaxMana());
            String currMana = String.valueOf(mana.getCurrentMana());
            String maxMana = String.valueOf(mana.getMaxMana());
            graphics.blit(RenderType::guiTextured, TEXTURE, xPos, yPos, 0, 0, BORDER_WIDTH, BORDER_HEIGHT, 256, 256);
            graphics.blit(RenderType::guiTextured, TEXTURE, xPos + 2, yPos + 2, 0, 18, currWidth, MANA_BAR_HEIGHT, 256, 256);

            graphics.drawString(minecraft.font, currMana + "/" + maxMana, xPos + 20, yPos + 5, 16777215);
        }
    }
}
