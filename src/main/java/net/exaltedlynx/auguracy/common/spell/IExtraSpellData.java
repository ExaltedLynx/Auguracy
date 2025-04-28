package net.exaltedlynx.auguracy.common.spell;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IExtraSpellData
{
    void toBuffer(RegistryFriendlyByteBuf buffer);

    void fromBuffer(RegistryFriendlyByteBuf buffer);
}
