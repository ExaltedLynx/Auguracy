package net.exaltedlynx.auguracy.common.data_attachments;

import com.mojang.serialization.Codec;
import net.exaltedlynx.auguracy.Auguracy;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AuguracyAttachments
{
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Auguracy.MODID);

    public static final Supplier<AttachmentType<int[]>> ELEMENT_LEVELS = ATTACHMENT_TYPES.register(
            "element_levels", () -> AttachmentType.builder(() -> new int[] {0, 0, 0, 0, 0}).serialize(new IntArraySerializer()).copyOnDeath().build()
    );

    public static final Supplier<AttachmentType<Integer>> MANA = ATTACHMENT_TYPES.register(
            "mana", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).copyOnDeath().build()
    );

    public static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
