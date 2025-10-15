package com.portingdeadmods.modjam.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MultiblockJEICategory implements IRecipeCategory<Multiblock> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Modjam.MODID, "multiblock");
    public static final RecipeType<Multiblock> RECIPE_TYPE = new RecipeType<>(UID, Multiblock.class);

    private final IDrawable icon;
    private final BlockRenderDispatcher blocks;

    private IGuiHelper guiHelper;
    private final IDrawableStatic background;
    private final IDrawableStatic slotDrawable;

    private boolean singleLayer = false;
    private int singleLayerOffset = 0;

    private ScreenArea explodeToggle;
    private ScreenArea layerUp;
    private ScreenArea layerSwap;
    private ScreenArea layerDown;

    private boolean exploded = false;
    private double explodeMulti = 1.0d;
    
    private float rotationX = 35f;
    private float rotationY = 0f;
    private float zoom = 1.0f;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private boolean isDragging = false;
    private ScreenArea renderArea;

    private final MutableComponent MATERIAL_COMPONENT = Component.translatable(Modjam.MODID + ".jei.multiblock.component")
            .withStyle(ChatFormatting.GRAY)
            .withStyle(ChatFormatting.ITALIC);

    public MultiblockJEICategory(IGuiHelper guiHelper) {
        int width = (9 * 18) + 10;
        int height = 60 + (10 + (18 * 3) + 5);

        this.guiHelper = guiHelper;
        this.background = guiHelper.createBlankDrawable(width, height);
        this.slotDrawable = guiHelper.getSlotDrawable();
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.STONE));

        this.blocks = Minecraft.getInstance().getBlockRenderer();
        
        int renderWidth = width - 10;
        int renderHeight = 82;
        int scissorX = 5;
        int scissorY = 0;
        this.renderArea = new ScreenArea(scissorX, scissorY, renderWidth, renderHeight);
        
        int controlsY = renderHeight;
        int centerX = width / 2;
        this.explodeToggle = new ScreenArea(centerX - 30, controlsY, 10, 10);
        this.layerSwap = new ScreenArea(centerX - 15, controlsY, 10, 10);
        this.layerUp = new ScreenArea(centerX, controlsY, 10, 10);
        this.layerDown = new ScreenArea(centerX + 15, controlsY, 10, 10);
    }

    @Override
    public RecipeType<Multiblock> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(Modjam.MODID + ".jei.multiblock.title");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder layout, Multiblock multiblock, IFocusGroup focuses) {
        singleLayer = false;
        singleLayerOffset = 0;
        rotationX = 35f;
        rotationY = 0f;
        zoom = 1.0f;
        isDragging = false;

        try {
            addMaterialSlots(multiblock, layout);
        } catch (Exception ex) {
            Modjam.LOGGER.error("Error displaying multiblock", ex);
        }
    }
    
    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, Multiblock multiblock, IFocusGroup focuses) {
        builder.addInputHandler(new MultiblockInputHandler());
    }
    
    private class MultiblockInputHandler implements IJeiInputHandler {
        @Override
        public ScreenRectangle getArea() {
            return new ScreenRectangle(renderArea.x, renderArea.y, renderArea.width, renderArea.height);
        }
        
        @Override
        public boolean handleInput(double mouseX, double mouseY, IJeiUserInput input) {
            InputConstants.Key key = input.getKey();
            if (key.getType() == InputConstants.Type.MOUSE && key.getValue() == 0) {
                if (renderArea.contains((int)mouseX, (int)mouseY)) {
                    if (input.isSimulate()) {
                        return true;
                    }
                    lastMouseX = mouseX;
                    lastMouseY = mouseY;
                    return true;
                }
            }
            
            return false;
        }
        
        @Override
        public boolean handleMouseDragged(double mouseX, double mouseY, InputConstants.Key mouseButton, double dragX, double dragY) {
            if (mouseButton.getValue() == 0) {
                isDragging = true;
                
                rotationY += (float)dragX * 2.0f;
                rotationX += (float)dragY * 2.0f;
                
                rotationX = Math.max(-90f, Math.min(90f, rotationX));
                
                return true;
            }
            isDragging = false;
            return false;
        }
        
        @Override
        public boolean handleMouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
            zoom = Math.max(0.3f, Math.min(3.0f, zoom + (float)scrollY * 0.1f));
            return true;
        }
    }

    private void addMaterialSlots(Multiblock multiblock, IRecipeLayoutBuilder layout) {
        AtomicInteger inputOffset = new AtomicInteger();

        final int GUTTER_X = 5;
        final int OFFSET_Y = 64;

        Map<Block, Integer> blockCounts = new HashMap<>();
        
        MultiblockLayer[] layers = multiblock.getLayout();
        for (MultiblockLayer layer : layers) {
            for (int blockId : layer.layer()) {
                Block block = multiblock.getDefinition().getDefaultBlock(blockId);
                if (block != null && block != Blocks.AIR) {
                    blockCounts.merge(block, 1, Integer::sum);
                }
            }
        }

        blockCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> {
                    Block block = entry.getKey();
                    int required = entry.getValue();
                    int finalInputOffset = inputOffset.get();

                    Item bi = block.asItem();

                    int slotX = GUTTER_X + (finalInputOffset % 9) * 18;
                    int slotY = (OFFSET_Y + 24) + ((finalInputOffset / 9) * 18);

                    final var slot = layout.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                            .setBackground(slotDrawable, -1, -1);

                    if (bi != Items.AIR) {
                        slot.addItemStack(new ItemStack(bi, required));
                        slot.addRichTooltipCallback((slots, c) -> c.add(MATERIAL_COMPONENT));
                        inputOffset.getAndIncrement();
                    }
                });

        for (int i = inputOffset.get(); i < 18; i++) {
            int slotX = GUTTER_X + (i % 9) * 18;
            int slotY = (OFFSET_Y + 24) + ((i / 9) * 18);

            layout.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).setBackground(slotDrawable, -1, -1);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, Multiblock multiblock, IRecipeSlotsView slots, double mouseX, double mouseY) {
        if (explodeToggle.contains((int)mouseX, (int)mouseY)) {
            if (!exploded) tooltip.add(Component.translatable(Modjam.MODID + ".jei.toggle_exploded_view"));
            else tooltip.add(Component.translatable(Modjam.MODID + ".jei.toggle_condensed_view"));
        }

        if (layerSwap.contains((int)mouseX, (int)mouseY)) {
            if (singleLayer) tooltip.add(Component.translatable(Modjam.MODID + ".jei.all_layers_mode"));
            else tooltip.add(Component.translatable(Modjam.MODID + ".jei.single_layer_mode"));
        }

        if (layerUp.contains((int)mouseX, (int)mouseY) && singleLayer) {
            if (singleLayerOffset < multiblock.getLayout().length - 1)
                tooltip.add(Component.translatable(Modjam.MODID + ".jei.layer_up"));
        }

        if (layerDown.contains((int)mouseX, (int)mouseY) && singleLayer) {
            if (singleLayerOffset > 0)
                tooltip.add(Component.translatable(Modjam.MODID + ".jei.layer_down"));
        }
    }

    @Override
    public boolean handleInput(Multiblock multiblock, double mouseX, double mouseY, InputConstants.Key input) {
        if (input.getType() == InputConstants.Type.MOUSE && input.getValue() == 0) {
            SoundManager handler = Minecraft.getInstance().getSoundManager();

            if (explodeToggle.contains((int)mouseX, (int)mouseY)) {
                explodeMulti = exploded ? 1.0d : 1.6d;
                exploded = !exploded;
                handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }

            if (layerSwap.contains((int)mouseX, (int)mouseY)) {
                singleLayer = !singleLayer;
                handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }

            if (layerUp.contains((int)mouseX, (int)mouseY) && singleLayer) {
                if (singleLayerOffset < multiblock.getLayout().length - 1) {
                    handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    singleLayerOffset++;
                }
                return true;
            }

            if (layerDown.contains((int)mouseX, (int)mouseY) && singleLayer) {
                if (singleLayerOffset > 0) {
                    handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    singleLayerOffset--;
                }

                return true;
            }
            
            if (renderArea.contains((int)mouseX, (int)mouseY)) {
                isDragging = true;
                lastMouseX = mouseX;
                lastMouseY = mouseY;
                return true;
            }
        }
        
        if (input.getType() == InputConstants.Type.MOUSE && input.getValue() == -1) {
            isDragging = false;
        }

        return false;
    }

    private void drawScaledTexture(
            GuiGraphics guiGraphics,
            ResourceLocation texture,
            ScreenArea area,
            float u, float v,
            int uWidth, int vHeight,
            int textureWidth, int textureHeight) {

        guiGraphics.blit(texture, area.x, area.y, area.width, area.height, u, v, uWidth, vHeight, textureWidth, textureHeight);
    }

    @Override
    public void draw(Multiblock multiblock, IRecipeSlotsView slots, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        PoseStack pose = guiGraphics.pose();

        Window mainWindow = Minecraft.getInstance().getWindow();
        double guiScaleFactor = mainWindow.getGuiScale();

        renderPreviewControls(guiGraphics, multiblock);

        renderRecipe(multiblock, guiGraphics, guiScaleFactor, renderArea);
        
        if (renderArea.contains((int)mouseX, (int)mouseY)) {
            guiGraphics.renderOutline(renderArea.x, renderArea.y, renderArea.width, renderArea.height, 0x80FFFFFF);
        }
    }

    private void renderRecipe(Multiblock multiblock, GuiGraphics guiGraphics, double guiScaleFactor, ScreenArea scissorBounds) {
        PoseStack mx = guiGraphics.pose();
        try {
            guiGraphics.fill(
                    scissorBounds.x, scissorBounds.y,
                    scissorBounds.x + scissorBounds.width,
                    scissorBounds.height,
                    0xFF404040
            );

            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();

            final double scale = Minecraft.getInstance().getWindow().getGuiScale();
            final Matrix4f matrix = mx.last().pose();
            final FloatBuffer buf = BufferUtils.createFloatBuffer(16);
            matrix.get(buf);

            int scaledX = (int)(scissorBounds.x * scale);
            int scaledY = (int)(scissorBounds.y * scale);
            int scaledWidth = (int)(scissorBounds.width * scale);
            int scaledHeight = (int)(scissorBounds.height * scale);
            final int scissorX = Math.round((float)(buf.get(12) * scale + scaledX));
            final int scissorY = Math.round((float)(Minecraft.getInstance().getWindow().getHeight() - scaledY - scaledHeight - buf.get(13) * scale));
            final int scissorW = Math.round((float)scaledWidth);
            final int scissorH = Math.round((float)scaledHeight);
            RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);

            mx.pushPose();

            mx.translate(
                    scissorBounds.x + (scissorBounds.width / 2.0),
                    scissorBounds.y + (scissorBounds.height / 2.0),
                    100);

            int totalLayers = multiblock.getLayout().length;
            float avgDim = (float) Math.sqrt(totalLayers * 9);
            float previewScale = (float) ((3 + Math.exp(3 - (avgDim / 5))) / explodeMulti) * zoom;
            mx.scale(previewScale, -previewScale, previewScale);

            drawActualMultiblock(multiblock, mx, buffers);

            mx.popPose();

            buffers.endBatch();

            RenderSystem.disableScissor();
        } catch (Exception ex) {
            Modjam.LOGGER.warn("Error rendering multiblock", ex);
        }
    }

    private void drawActualMultiblock(Multiblock multiblock, PoseStack mx, MultiBufferSource.BufferSource buffers) {
        mx.mulPose(new Quaternionf().rotationXYZ(
                (float) Math.toRadians(rotationX),
                (float) Math.toRadians(rotationY),
                0
        ));

        MultiblockLayer[] layers = multiblock.getLayout();

        int[] renderLayers;
        if (!singleLayer) {
            renderLayers = IntStream.range(0, layers.length).toArray();
        } else {
            renderLayers = new int[]{singleLayerOffset};
        }

        IntIntPair avgWidth = IntIntPair.of(3, 3);
        if (layers.length > 0) {
            avgWidth = layers[0].getWidths();
        }

        mx.translate(
                -(avgWidth.leftInt() / 2.0) * explodeMulti - 0.5,
                -(layers.length / 2.0) * explodeMulti - 0.5,
                -(avgWidth.rightInt() / 2.0) * explodeMulti - 0.5
        );

        for (int y : renderLayers) {
            if (y < layers.length) {
                renderMultiblockLayer(multiblock, mx, buffers, layers[y], y);
            }
        }
    }

    private void renderPreviewControls(GuiGraphics guiGraphics, Multiblock multiblock) {
        PoseStack mx = guiGraphics.pose();
        mx.pushPose();
        mx.translate(0, 0, 10);

        ResourceLocation sprites = ResourceLocation.fromNamespaceAndPath(Modjam.MODID, "textures/gui/jei-sprites.png");

        if (exploded) {
            drawScaledTexture(guiGraphics, sprites, explodeToggle, 16, 0, 16, 16, 96, 16);
        } else {
            drawScaledTexture(guiGraphics, sprites, explodeToggle, 0, 0, 16, 16, 96, 16);
        }

        if (singleLayer) {
            drawScaledTexture(guiGraphics, sprites, layerSwap, 48, 0, 16, 16, 96, 16);
        } else {
            drawScaledTexture(guiGraphics, sprites, layerSwap, 32, 0, 16, 16, 96, 16);
        }

        if (singleLayer) {
            if (singleLayerOffset < multiblock.getLayout().length - 1)
                drawScaledTexture(guiGraphics, sprites, layerUp, 64, 0, 16, 16, 96, 16);

            if (singleLayerOffset > 0) {
                drawScaledTexture(guiGraphics, sprites, layerDown, 80, 0, 16, 16, 96, 16);
            }
        }

        mx.popPose();
    }

    private void renderMultiblockLayer(Multiblock multiblock, PoseStack mx, MultiBufferSource.BufferSource buffers, MultiblockLayer layer, int layerY) {
        mx.pushPose();

        IntIntPair widths = layer.getWidths();
        int xWidth = widths.leftInt();
        int zWidth = widths.rightInt();

        for (int i = 0; i < layer.layer().length; i++) {
            int blockId = layer.layer()[i];

            int x = i % xWidth;
            int z = i / xWidth;

            mx.pushPose();

            mx.translate(
                    (x + 0.5) * explodeMulti,
                    (layerY + 0.5) * explodeMulti,
                    (z + 0.5) * explodeMulti
            );

            Block block = multiblock.getDefinition().getDefaultBlock(blockId);
            if (block != null && block != Blocks.AIR) {
                renderBlock(mx, buffers, block.defaultBlockState());
            }

            mx.popPose();
        }

        mx.popPose();
    }

    private void renderBlock(PoseStack mx, MultiBufferSource.BufferSource buffers, BlockState state) {
        try {
            blocks.renderSingleBlock(state,
                    mx,
                    buffers,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY, null);
        } catch (Exception e) {
            Modjam.LOGGER.warn("Error rendering block in preview: {}", state);
            Modjam.LOGGER.error("Stack Trace", e);
        }
    }

    private static class ScreenArea {
        int x, y, width, height;

        ScreenArea(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean contains(int px, int py) {
            return px >= x && px <= x + width && py >= y && py <= y + height;
        }
    }
}
