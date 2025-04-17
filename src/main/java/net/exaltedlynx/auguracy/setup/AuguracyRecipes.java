package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.recipe.ShapelessSpellItemRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AuguracyRecipes
{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Auguracy.MODID);

    public static final Supplier<RecipeType<ShapelessSpellItemRecipe>> SHAPELESS_SPELL_ITEM_RECIPE = RECIPE_TYPES.register(
            "shapeless_spell_item", registryName -> new RecipeType<>() {
                @Override
                public String toString() {
                    return registryName.toString();
                }
            }
    );
}
