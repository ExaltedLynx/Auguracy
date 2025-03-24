package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AuguracyCreativeTab
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Auguracy.MODID);

    public static final Supplier<CreativeModeTab> AUGURACY_TAB = CREATIVE_MODE_TABS.register(Auguracy.MODID, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(AuguracyBlocks.MANA_FLOWER))
            .title(Component.translatable("auguracy.creativetab"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(AuguracyBlocks.MANA_FLOWER);
                output.accept(AuguracyItems.MANA_SHARD);
            })
            .build()
    );

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
