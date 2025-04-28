package net.exaltedlynx.auguracy.common.datagen;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.datagen.builders.SpellInscriberRecipeBuilder;
import net.exaltedlynx.auguracy.common.recipe.SpellInscriberRecipe;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class AuguracyRecipeProvider extends RecipeProvider
{
	protected AuguracyRecipeProvider(HolderLookup.Provider provider, RecipeOutput output)
	{
		super(provider, output);
	}

	@Override
	protected void buildRecipes()
	{
		new SpellInscriberRecipeBuilder(SpellInscriberRecipe::new)
				.setSpellResult(AuguracySpells.DIG.get())
				.addIngredient(tag(ItemTags.PICKAXES))
				.save(this.output, "dig_spell");

		new SpellInscriberRecipeBuilder(SpellInscriberRecipe::new)
				.setSpellResult(AuguracySpells.EMPTY.get())
				.addIngredient(Ingredient.of(Items.APPLE))
				.save(this.output, "test_spell");
	}

	public static class Runner extends RecipeProvider.Runner
	{
		protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
		{
			super(packOutput, lookupProvider);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output)
		{
			return new AuguracyRecipeProvider(provider, output);
		}

		@Override
		public String getName()
		{
			return Auguracy.MODID + " Recipes";
		}
	}
}
