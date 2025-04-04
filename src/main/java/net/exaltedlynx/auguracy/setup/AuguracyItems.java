package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.items.DivineWand;
import net.exaltedlynx.auguracy.common.items.SpellCrystal;
import net.exaltedlynx.auguracy.common.items.SpellScroll;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AuguracyItems
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(Auguracy.MODID);

    public static final DeferredItem<Item> MANA_SHARD = ITEMS.registerSimpleItem("mana_shard", new Item.Properties());
    public static final DeferredItem<Item> DIVINE_WAND = ITEMS.registerItem("divine_wand", DivineWand::new);
    public static final DeferredItem<Item> SPELL_SCROLL = ITEMS.registerItem("spell_scroll", SpellScroll::new);
    public static final DeferredItem<Item> SPELL_CRYSTAL = ITEMS.registerItem("spell_crystal", SpellCrystal::new);

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
