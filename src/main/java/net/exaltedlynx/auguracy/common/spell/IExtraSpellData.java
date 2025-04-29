package net.exaltedlynx.auguracy.common.spell;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IExtraSpellData<S extends Spell>
{
    void toBuffer(RegistryFriendlyByteBuf byteBuf);

    //S fromBuffer(RegistryFriendlyByteBuf buffer);


}
