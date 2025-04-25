package net.exaltedlynx.auguracy.common.datagen.builders;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.recipe.SpellInscriberInput;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SpellInscriberRecipeBuilder
{
    private final BiFunction<List<Ingredient>, Spell, Recipe<SpellInscriberInput>> factory;
    private Spell spellResult;
    private List<Ingredient> ingredients = new ArrayList<>();

    public SpellInscriberRecipeBuilder(BiFunction<List<Ingredient>, Spell, Recipe<SpellInscriberInput>> factory)
    {
        this.factory = factory;
    }

    public SpellInscriberRecipeBuilder addIngredient(Ingredient input)
    {
        this.ingredients.add(input);
        return this;
    }

    public SpellInscriberRecipeBuilder setSpellResult(Spell spellResult)
    {
        this.spellResult = spellResult;
        return this;
    }

    public void save(RecipeOutput output, String recipeId)
    {
        this.save(output, ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, recipeId)));
    }

    public void save(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        output.accept(key, this.factory.apply(ingredients, spellResult), null);
    }
}
