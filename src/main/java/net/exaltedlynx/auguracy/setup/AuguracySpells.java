package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.common.spell.spells.Spells.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class AuguracySpells
{
    public static final ResourceKey<Registry<Spell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "spells"));
    public static final Registry<Spell> SPELL_REGISTRY = new RegistryBuilder<>(SPELL_REGISTRY_KEY).sync(true).create();
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(SPELL_REGISTRY, Auguracy.MODID);

    public static final Supplier<Spell> DIG = SPELLS.register("dig_spell", DigSpell::new);


}
