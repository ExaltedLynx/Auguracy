package net.exaltedlynx.auguracy.common.data_attachments;

import net.exaltedlynx.auguracy.Auguracy;
import net.exaltedlynx.auguracy.common.data_attachments.elements.ElementLevels;
import net.exaltedlynx.auguracy.common.data_attachments.mana.Mana;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AuguracyAttachments
{
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Auguracy.MODID);

    public static final Supplier<AttachmentType<ElementLevels>> ELEMENT_LEVELS = ATTACHMENT_TYPES.register(
            "element_levels", () -> AttachmentType.builder(ElementLevels::new).serialize(ElementLevels.CODEC).copyOnDeath().build()
    );

    public static final Supplier<AttachmentType<Mana>> MANA = ATTACHMENT_TYPES.register(
            "mana", () -> AttachmentType.builder(Mana::new).serialize(Mana.CODEC).copyOnDeath().build()
    );

    public static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
