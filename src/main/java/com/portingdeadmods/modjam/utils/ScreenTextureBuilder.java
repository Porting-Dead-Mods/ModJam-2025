package com.portingdeadmods.modjam.utils;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ScreenTextureBuilder {
    private final String modId;
    private final String name;
    private final List<SlotData> slots = new ArrayList<>();
    private static final Map<String, ResourceLocation> textureCache = new HashMap<>();

    private static final int DEFAULT_GUI_WIDTH = 176;
    private static final int DEFAULT_GUI_HEIGHT = 166;
    private static final int DEFAULT_SLOT_SIZE = 18;
    private static final int STANDARD_SLOT_SPACING = 18;
    private static final int SLOT_OFFSET_X = 0;
    private static final int SLOT_OFFSET_Y = 0;
    private static final int BACKGROUND_OFFSET_Y = -1;
    
    private ScreenTextureBuilder(String modId, String name) {
        this.modId = modId;
        this.name = name;
    }
    
    public static ScreenTextureBuilder create(String modId, String name) {
        return new ScreenTextureBuilder(modId, name);
    }
    
    public ScreenTextureBuilder addSlot(int x, int y) {
        return addSlot(x, y, null);
    }
    
    public ScreenTextureBuilder addSlot(int x, int y, ResourceLocation customTexture) {
        slots.add(new SlotData(x, y, customTexture));
        return this;
    }
    
    public ScreenTextureBuilder addSlotRect(int x, int y, int cols, int rows, int xSpacing, int ySpacing) {
        return addSlotRect(x, y, cols, rows, xSpacing, ySpacing, null);
    }
    
    public ScreenTextureBuilder addSlotRect(int x, int y, int cols, int rows, int xSpacing, int ySpacing, ResourceLocation customTexture) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int slotX = x + (col * xSpacing);
                int slotY = y + (row * ySpacing);
                addSlot(slotX, slotY, customTexture);
            }
        }
        return this;
    }
    
    public ScreenTextureBuilder addCenteredSlotRect(int y, int cols, int rows) {
        return addCenteredSlotRect(y, cols, rows, STANDARD_SLOT_SPACING, STANDARD_SLOT_SPACING);
    }
    
    public ScreenTextureBuilder addCenteredSlotRect(int y, int cols, int rows, int xSpacing, int ySpacing) {
        int totalWidth = (cols * xSpacing) - (xSpacing - DEFAULT_SLOT_SIZE);
        int x = (DEFAULT_GUI_WIDTH - totalWidth) / 2;
        return addSlotRect(x, y, cols, rows, xSpacing, ySpacing);
    }
    
    public int getSlotCount() {
        return slots.size();
    }
    
    public ResourceLocation buildAndCache() {
        String cacheKey = modId + ":" + name + "_" + generateSlotHash();
        
        if (textureCache.containsKey(cacheKey)) {
            return textureCache.get(cacheKey);
        }
        
        if (!isClientReady()) {
            throw new IllegalStateException("Cannot build GUI textures before client is initialized");
        }
        
        try {
            NativeImage compositeImage = createCompositeTexture();
            ResourceLocation textureId = ResourceLocation.fromNamespaceAndPath(modId, "gui/generated/" + name);
            
            DynamicTexture dynamicTexture = new DynamicTexture(compositeImage);
            Minecraft.getInstance().getTextureManager().register(textureId, dynamicTexture);
            
            textureCache.put(cacheKey, textureId);
            return textureId;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to build GUI texture: " + cacheKey, e);
        }
    }
    
    private NativeImage createCompositeTexture() throws IOException {
        NativeImage originalBase = loadBaseTexture();
        NativeImage compositeImage = new NativeImage(originalBase.getWidth(), originalBase.getHeight(), false);
        
        blitImage(compositeImage, originalBase, 0, BACKGROUND_OFFSET_Y);
        
        NativeImage slotTexture = loadDefaultSlotTexture();
        
        for (SlotData slot : slots) {
            NativeImage currentSlotTexture = slot.customTexture != null ? 
                loadTexture(slot.customTexture) : slotTexture;
            
            blitImage(compositeImage, currentSlotTexture, slot.x + SLOT_OFFSET_X, slot.y + SLOT_OFFSET_Y + BACKGROUND_OFFSET_Y);
        }
        
        originalBase.close();
        
        return compositeImage;
    }
    
    private NativeImage loadBaseTexture() throws IOException {
        ResourceLocation baseLocation = ResourceLocation.fromNamespaceAndPath(modId, "textures/gui/empty_gui.png");
        return loadTexture(baseLocation);
    }
    
    private NativeImage loadDefaultSlotTexture() throws IOException {
        ResourceLocation slotLocation = ResourceLocation.fromNamespaceAndPath(modId, "textures/gui/slot.png");
        return loadTexture(slotLocation);
    }
    
    private NativeImage loadTexture(ResourceLocation location) throws IOException {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        try (InputStream stream = resourceManager.getResource(location).orElseThrow().open()) {
            return NativeImage.read(stream);
        }
    }
    
    private void blitImage(NativeImage dest, NativeImage src, int destX, int destY) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int destWidth = dest.getWidth();
        int destHeight = dest.getHeight();
        
        for (int x = 0; x < srcWidth && destX + x < destWidth; x++) {
            for (int y = 0; y < srcHeight && destY + y < destHeight; y++) {
                if (destX + x >= 0 && destY + y >= 0) {
                    int pixel = src.getPixelRGBA(x, y);
                    int alpha = (pixel >> 24) & 0xFF;
                    
                    if (alpha > 0) {
                        dest.setPixelRGBA(destX + x, destY + y, pixel);
                    }
                }
            }
        }
    }
    
    private String generateSlotHash() {
        StringBuilder sb = new StringBuilder();
        for (SlotData slot : slots) {
            sb.append(slot.x).append(",").append(slot.y).append(",")
              .append(slot.customTexture != null ? slot.customTexture.toString() : "default")
              .append(";");
        }
        return Integer.toHexString(sb.toString().hashCode());
    }
    
    private static boolean isClientReady() {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            return minecraft != null && minecraft.getResourceManager() != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void clearCache() {
        textureCache.clear();
    }
    
    private static class SlotData {
        final int x, y;
        final ResourceLocation customTexture;
        
        SlotData(int x, int y, ResourceLocation customTexture) {
            this.x = x;
            this.y = y;
            this.customTexture = customTexture;
        }
    }
}
