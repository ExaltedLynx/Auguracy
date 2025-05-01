package net.exaltedlynx.auguracy.common.datagen;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.setup.AuguracyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class AuguracyTagsProvider
{
	public static class Items extends ItemTagsProvider
	{
		public static final TagKey<Item> SPELL_CASTER_ITEM_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "spell_caster"));

		public Items(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags)
		{
			super(output, lookupProvider, blockTags, Auguracy.MODID);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider)
		{
			this.tag(SPELL_CASTER_ITEM_TAG).add(AuguracyItems.SPELL_SCROLL.get(), AuguracyItems.DIVINE_WAND.get());
		}
	}

	public static class Blocks extends BlockTagsProvider
	{
		public Blocks(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
		{
			super(output, lookupProvider, Auguracy.MODID);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider)
		{

		}
	}
}
