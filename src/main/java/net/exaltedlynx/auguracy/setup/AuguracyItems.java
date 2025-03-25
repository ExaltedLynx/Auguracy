package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AuguracyItems
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(Auguracy.MODID);

    public static final DeferredItem<Item> MANA_SHARD = ITEMS.registerSimpleItem("mana_shard", new Item.Properties());
    public static final DeferredItem<Item> DIVINE_WAND = ITEMS.registerSimpleItem("divine_wand");
    public static final DeferredItem<Item> SPELL_SCROLL = ITEMS.registerSimpleItem("spell_scroll");
    public static final DeferredItem<Item> SPELL_CRYSTAL = ITEMS.registerSimpleItem("spell_crystal");

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
