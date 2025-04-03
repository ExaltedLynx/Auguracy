package net.exaltedlynx.auguracy.setup;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementType;
import net.exaltedlynx.auguracy.common.spell.Spell;
import net.exaltedlynx.auguracy.common.spell.Spells.*;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class AuguracySpells
{
    public static final ResourceKey<Registry<Spell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Auguracy.MODID, "spells"));
    public static final Registry<Spell> SPELL_REGISTRY = new RegistryBuilder<>(SPELL_REGISTRY_KEY).sync(true).create();
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(SPELL_REGISTRY, Auguracy.MODID);

    public static final Supplier<Spell> DIG = SPELLS.register("dig_spell", () -> new DigSpell("dig", ElementType.EARTH, 1, 2));

    //fallback in case a spell is not found
    public static final Supplier<Spell> EMPTY = SPELLS.register("empty_spell", () -> new Spell() {
        { name = "empty"; type = ElementType.FIRE; lvlReq = 0; manaCost = 0; }
        @Override
        protected void onCast(Player caster) {
            Auguracy.LOGGER.atDebug().log("casted spell");
            caster.displayClientMessage(Component.literal("This is contains empty spell: Someone made an oopsie"), false);
        }
    });

    public static Spell getSpellFromName(String spellName)
    {
        for (DeferredHolder<Spell, ? extends Spell> spellDeferredHolder : SPELLS.getEntries()) {
            Spell spell = spellDeferredHolder.get();
            if (spell.getName().equals(spellName))
                return spell;
        }
        return EMPTY.get();
    }

    public static void register(IEventBus eventBus)
    {
        SPELLS.register(eventBus);
    }
}