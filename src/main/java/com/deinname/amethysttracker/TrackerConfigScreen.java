package com.deinname.amethysttracker;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TrackerConfigScreen extends Screen {

    protected TrackerConfigScreen() {
        super(Text.literal("Amethyst Tracker Menü"));
    }

    @Override
    protected void init() {
        super.init();

        String buttonText = "Tracking: " + (AmethystTrackerClient.trackingEnabled ? "AN" : "AUS");

        this.addDrawableChild(ButtonWidget.builder(Text.literal(buttonText), button -> {
            AmethystTrackerClient.trackingEnabled = !AmethystTrackerClient.trackingEnabled;
            button.setMessage(Text.literal("Tracking: " + (AmethystTrackerClient.trackingEnabled ? "AN" : "AUS")));
        }).dimensions(this.width / 2 - 100, this.height / 2 - 20, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Schließen"), button -> {
            this.close();
        }).dimensions(this.width / 2 - 100, this.height / 2 + 10, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 60, 0xFF69B4);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
