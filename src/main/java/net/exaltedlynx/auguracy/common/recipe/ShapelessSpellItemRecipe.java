package net.exaltedlynx.auguracy.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.items.components.SpellContainer;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.common.spell.Spells;
import net.exaltedlynx.auguracy.setup.AuguracyDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;

public class ShapelessSpellItemRecipe extends ShapelessRecipe {

    private final Spell spellToAttach;
    private final ItemStack result;

    public ShapelessSpellItemRecipe(String group, CraftingBookCategory category, ItemStack result, List<Ingredient> ingredients, Spell spellToAttach)
    {
        super(group, category, result, ingredients);
        this.result = result;
        this.spellToAttach = spellToAttach;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider)
    {
        if(spellToAttach instanceof Spells.DigSpell digSpell)
        {
            for (var item : input.items())
            {
                if(item.is(ItemTags.PICKAXES))
                {
                    digSpell.setPickaxe(item);
                    result.set(AuguracyDataComponents.SPELL_CONTAINER, new SpellContainer(digSpell));
                }
            }
        }
        else
        {
            result.set(AuguracyDataComponents.SPELL_CONTAINER, new SpellContainer(spellToAttach));
        }

        return super.assemble(input, provider);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public class Serializer implements RecipeSerializer<ShapelessSpellItemRecipe>
    {
        /*
        public static final MapCodec<ShapelessSpellItemRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_301133_ -> p_301133_.category),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(p_301142_ -> p_301142_.result),
                Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, ShapedRecipePattern.maxHeight * ShapedRecipePattern.maxWidth)).fieldOf("ingredients").forGetter(p_360071_ -> p_360071_.ingredients)
        ))
         */

        @Override
        public MapCodec<ShapelessSpellItemRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapelessSpellItemRecipe> streamCodec() {
            return null;
        }
    }
}
