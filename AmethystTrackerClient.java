package com.deinname.amethysttracker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AmethystTrackerClient implements ClientModInitializer {
    private static KeyBinding menuKeyBinding;
    public static boolean trackingEnabled = false;

    @Override
    public void onInitializeClient() {
        // Taste 'O' zum Öffnen des Menüs / Umschalten
        menuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.amethysttracker.menu",
                InputUtil.Type.KEY_SYM,
                GLFW.GLFW_KEY_O,
                "category.amethysttracker.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKeyBinding.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new TrackerConfigScreen());
                }
            }
        });

        // Welt-Rendering-Event für die pinken Quadrate
        WorldRenderEvents.LAST.register(TrackerRenderer::render);
    }
}
