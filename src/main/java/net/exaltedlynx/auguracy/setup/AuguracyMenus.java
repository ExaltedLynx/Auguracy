package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.containers.SpellInscriberMenu;
import net.exaltedlynx.auguracy.common.containers.WandMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AuguracyMenus
{
	public static final DeferredRegister<MenuType<?>> AUGURACY_MENUS = DeferredRegister.create(Registries.MENU, Auguracy.MODID);

	public static final Supplier<MenuType<SpellInscriberMenu>> SPELL_INSCRIBER_MENU = AUGURACY_MENUS.register("inscriber_menu", () -> new MenuType<>(SpellInscriberMenu::createClientMenu, FeatureFlags.DEFAULT_FLAGS));

	public static final Supplier<MenuType<WandMenu>> WAND_MENU = AUGURACY_MENUS.register("wand_menu", () -> new MenuType<>(WandMenu::createClientMenu, FeatureFlags.DEFAULT_FLAGS));

	public static void register(IEventBus event)
	{
		AUGURACY_MENUS.register(event);
	}

}
