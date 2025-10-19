package com.portingdeadmods.modjam.client.screens.widgets;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class ModifiableFittingMultiLineTextWidget extends AbstractScrollWidget {
    private final Font font;
    private final MultiLineTextWidget multilineWidget;
    private boolean borderVisible;

    public ModifiableFittingMultiLineTextWidget(int x, int y, int width, int height, Component message, Font font) {
        super(x, y, width, height, message);
        this.font = font;
        this.multilineWidget = (new MultiLineTextWidget(message, font)).setMaxWidth(this.getWidth() - this.totalInnerPadding());
    }

    public void setBorderVisible(boolean borderVisible) {
        this.borderVisible = borderVisible;
    }

    public ModifiableFittingMultiLineTextWidget setColor(int color) {
        this.multilineWidget.setColor(color);
        return this;
    }

    @Override
    public void setMessage(Component message) {
        super.setMessage(message);
        this.multilineWidget.setMessage(message);
    }

    public void setWidth(int width) {
        super.setWidth(width);
        this.multilineWidget.setMaxWidth(this.getWidth() - this.totalInnerPadding());
    }

    protected int getInnerHeight() {
        return this.multilineWidget.getHeight();
    }

    protected double scrollRate() {
        return (double) 9.0F;
    }

    protected void renderBackground(GuiGraphics guiGraphics) {
        if (this.borderVisible) {
            if (this.scrollbarVisible()) {
                super.renderBackground(guiGraphics);
            } else if (this.isFocused()) {
                this.renderBorder(guiGraphics, this.getX() - this.innerPadding(), this.getY() - this.innerPadding(), this.getWidth() + this.totalInnerPadding(), this.getHeight() + this.totalInnerPadding());
            }
        }

    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.visible) {
            if (!this.scrollbarVisible()) {
                this.renderBackground(guiGraphics);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) this.getX(), (float) this.getY(), 0.0F);
                this.multilineWidget.render(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.pose().popPose();
            } else {
                super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

    }

    public boolean showingScrollBar() {
        return super.scrollbarVisible();
    }

    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) (this.getX() + this.innerPadding()), (float) (this.getY() + 1), 0.0F);
        this.multilineWidget.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().popPose();
    }

    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.getMessage());
    }
}