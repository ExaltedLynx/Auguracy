package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AuguracyDataComponents
{
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Auguracy.MODID);

    public static final Supplier<DataComponentType<SpellContainer>> SPELL_CONTAINER = COMPONENTS.registerComponentType(
            "spell_container", builder -> builder.persistent(SpellContainer.CODEC).networkSynchronized(SpellContainer.STREAM_CODEC));

    public static final Supplier<DataComponentType<ItemContainerContents>> WAND_ITEM_HANDLER = COMPONENTS.registerComponentType(
            "wand_item_handler", builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC));

    public static void register(IEventBus eventBus)
    {
        COMPONENTS.register(eventBus);
    }
}
