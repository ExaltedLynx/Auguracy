package net.exaltedlynx.auguracy.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.exaltedlynx.auguracy.common.network.OnCastReleasePacket;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClientInputHandler
{
    private static boolean rightMousePressed;
    private static boolean rightMouseReleased;

    @SubscribeEvent
    public static void onMouseInput(InputEvent.InteractionKeyMappingTriggered event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if(player == null)
            return;

        ItemStack usedItem = player.getItemInHand(event.getHand());
        if(event.isUseItem() && usedItem.has(AuguracyDataComponents.SPELL_CONTAINER))
        {
            event.setSwingHand(false);
            if(rightMouseReleased)
            {
                PacketDistributor.sendToServer(new OnCastReleasePacket(usedItem.get(AuguracyDataComponents.SPELL_CONTAINER).spell()));
                rightMouseReleased = false;
            }
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event)
    {
        handleRightMouseInput(event.getButton(), event.getAction());
    }

    private static void handleRightMouseInput(int button, int action)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if(player == null)
            return;

        if(button == InputConstants.MOUSE_BUTTON_RIGHT)
        {
            if(action == InputConstants.RELEASE)
            {
                rightMousePressed = false;
                rightMouseReleased = true;
            }
            else if (action == InputConstants.PRESS)
            {
                rightMousePressed = true;
            }
        }
    }
}
