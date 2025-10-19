package com.portingdeadmods.modjam.registries;

import com.mojang.serialization.Codec;
import com.portingdeadmods.modjam.Modjam;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MJDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Modjam.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> HAS_RECEIVED_GUIDE = ATTACHMENTS.register(
            "has_received_guide", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build()
    );
}
