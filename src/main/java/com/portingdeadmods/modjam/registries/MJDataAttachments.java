package com.portingdeadmods.modjam.registries;

import com.portingdeadmods.modjam.Modjam;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MJDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = 
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Modjam.MODID);

}
