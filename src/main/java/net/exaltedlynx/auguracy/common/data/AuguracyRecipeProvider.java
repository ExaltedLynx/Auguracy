package net.exaltedlynx.auguracy.common.data;

import net.exaltedlynx.auguracy.Auguracy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.crafting.RepairItemRecipe;

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
		//SpecialRecipeBuilder.special(RepairItemRecipe::new);
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
