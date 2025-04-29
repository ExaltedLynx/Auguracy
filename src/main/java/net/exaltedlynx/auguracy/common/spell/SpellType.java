package net.exaltedlynx.auguracy.common.spell;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SpellType(MapCodec<? extends Spell> codec, StreamCodec<? super RegistryFriendlyByteBuf, ? extends Spell> streamCodec) { }
