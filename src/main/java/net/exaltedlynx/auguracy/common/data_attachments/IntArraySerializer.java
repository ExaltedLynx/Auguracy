package net.exaltedlynx.auguracy.common.data_attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.exaltedlynx.auguracy.common.blocks.ManaFlower;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntArrayTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public class IntArraySerializer implements IAttachmentSerializer<IntArrayTag, int[]>
{
    @Override
    public int[] read(IAttachmentHolder holder, IntArrayTag tag, HolderLookup.Provider provider) {
        return tag.getAsIntArray();
    }

    @Override
    public @Nullable IntArrayTag write(int[] attachment, HolderLookup.Provider provider) {
        return new IntArrayTag(attachment);
    }
}
