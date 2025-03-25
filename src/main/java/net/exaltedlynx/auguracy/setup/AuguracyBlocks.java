package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.blocks.ManaFlower;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class AuguracyBlocks
{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Auguracy.MODID);

    public static final DeferredBlock<Block> MANA_FLOWER = registerBlock("mana_flower", factory -> new ManaFlower(MobEffects.GLOWING, 5, factory),
            BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> block, BlockBehaviour.Properties properties)
    {
        DeferredBlock<T> deferredBlock = BLOCKS.registerBlock(name, block, properties.setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, name))));
        registerBlockItem(name, deferredBlock);
        return deferredBlock;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        AuguracyItems.ITEMS.registerSimpleBlockItem(name, block);
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
