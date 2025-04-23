package net.exaltedlynx.auguracy;

import net.exaltedlynx.auguracy.client.gui.GuiEventHandler;
import net.exaltedlynx.auguracy.common.data_attachments.AuguracyAttachments;
import net.exaltedlynx.auguracy.common.events.PlayerEventHandler;
import net.exaltedlynx.auguracy.common.network.NetworkRegister;
import net.exaltedlynx.auguracy.setup.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@Mod(Auguracy.MODID)
public class Auguracy
{
    public static final String MODID = "auguracy";
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Auguracy(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(PlayerEventHandler.class);

        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(NetworkRegister::register);
        modEventBus.addListener(GuiEventHandler::registerHUDOverlays);
        modEventBus.addListener(GuiEventHandler::registerScreens);

        AuguracyCreativeTab.register(modEventBus);
        AuguracyDataComponents.register(modEventBus);
        AuguracyAttachments.register(modEventBus);
        AuguracyBlocks.register(modEventBus);
        AuguracyItems.register(modEventBus);
        AuguracySpells.register(modEventBus);
        AuguracyMenus.register(modEventBus);
        AuguracyRecipes.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    public void registerRegistries(NewRegistryEvent event)
    {
        event.register(AuguracySpells.SPELL_REGISTRY);
        event.register(AuguracySpells.SPELL_TYPES_REGISTRY);
    }
}
