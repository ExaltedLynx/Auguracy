package net.exaltedlynx.auguracy.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.common.spell.Spells.*;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.exaltedlynx.auguracy.setup.AuguracyRecipes;
import net.exaltedlynx.auguracy.setup.AuguracySpells;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class SpellInscriberRecipe implements Recipe<SpellInscriberInput>
{
    private final List<Ingredient> ingredients;
    private final Spell spellToAttach;

    public SpellInscriberRecipe(List<Ingredient> ingredients, Spell spellToAttach)
    {
        this.ingredients = ingredients;
        this.spellToAttach = spellToAttach;
    }

    public SpellInscriberRecipe(List<Ingredient> ingredients, String spellToAttach)
    {
        this.ingredients = ingredients;
        this.spellToAttach = AuguracySpells.getSpellFromName(spellToAttach);
    }

    @Override
    public boolean matches(SpellInscriberInput input, Level level)
    {
        if(input.size() != ingredients.size())
            return false;
        else
        {
            var nonEmptyItems = new ArrayList<ItemStack>(input.size());
            for(var item : input.items())
            {
                if(!item.isEmpty())
                    nonEmptyItems.add(item);
            }
            return RecipeMatcher.findMatches(nonEmptyItems, ingredients) != null;
        }
    }

    @Override
    public ItemStack assemble(SpellInscriberInput input, HolderLookup.Provider registries) {
        ItemStack result = input.spellItem().getDefaultInstance();
        if(spellToAttach instanceof DigSpell digSpell)
        {
            for (var item : input.items())
            {
                if(item.is(ItemTags.PICKAXES))
                {
                    digSpell.setPickaxe(item);
                    result.update(AuguracyDataComponents.SPELL_CONTAINER, SpellContainer.EMPTY.get(), digSpell, SpellContainer::setNewSpell);
                }
            }
        }
        else
            result.update(AuguracyDataComponents.SPELL_CONTAINER, SpellContainer.EMPTY.get(), spellToAttach, SpellContainer::setNewSpell);

        result.set(DataComponents.CUSTOM_NAME, Component.literal(spellToAttach.getName()));
        return result.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public PlacementInfo placementInfo()
    {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<SpellInscriberInput>> getSerializer() {
        return AuguracyRecipes.INSCRIBER_RECIPE.get();
    }

    @Override
    public RecipeType<? extends Recipe<SpellInscriberInput>> getType() {
        return AuguracyRecipes.INSCRIBER_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<SpellInscriberRecipe>
    {
        public static final MapCodec<SpellInscriberRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf(1, 6).fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                Codec.STRING.fieldOf("spell_to_attach").forGetter(recipe -> recipe.spellToAttach.getName())
        ).apply(inst, SpellInscriberRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SpellInscriberRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            recipe -> recipe.ingredients,
            ByteBufCodecs.STRING_UTF8,
            recipe -> recipe.spellToAttach.getName(),
            SpellInscriberRecipe::new
        );

        @Override
        public MapCodec<SpellInscriberRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SpellInscriberRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
