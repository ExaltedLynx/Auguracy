package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.recipe.SpellInscriberRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AuguracyRecipes
{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Auguracy.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Auguracy.MODID);

    public static final Supplier<RecipeType<SpellInscriberRecipe>> INSCRIBER_RECIPE_TYPE = RECIPE_TYPES.register(
            "inscriber", registryName -> new RecipeType<>() {
                @Override
                public String toString() {
                    return registryName.toString();
                }
            }
    );

    public static final Supplier<RecipeSerializer<SpellInscriberRecipe>> INSCRIBER_RECIPE = RECIPE_SERIALIZERS.register("inscriber", SpellInscriberRecipe.Serializer::new);

    public static void register(IEventBus bus)
    {
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
}
