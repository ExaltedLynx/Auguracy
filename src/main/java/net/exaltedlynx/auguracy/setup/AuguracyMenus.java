package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.client.gui.menus.SpellInscriberMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AuguracyMenus
{
	public static final DeferredRegister<MenuType<?>> AUGURACY_MENUS = DeferredRegister.create(Registries.MENU, Auguracy.MODID);

	public static final Supplier<MenuType<SpellInscriberMenu>> SPELL_INSCRIBER_MENU = AUGURACY_MENUS.register("inscriber_menu", () -> new MenuType<>(SpellInscriberMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
