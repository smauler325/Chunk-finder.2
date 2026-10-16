package com.deinname.amethysttracker;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import org.joml.Matrix4f;

public class TrackerRenderer {

    public static void render(WorldRenderContext context) {
        if (!AmethystTrackerClient.trackingEnabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        Vec3d cameraPos = context.camera().getPos();
        MatrixStack matrices = context.matrixStack();
        matrices.push();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.lineWidth(4.0F);

        int playerChunkX = client.player.getChunkPos().x;
        int playerChunkZ = client.player.getChunkPos().z;
        int renderDistance = 8; // Suchradius in Chunks

        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {
                int chunkX = playerChunkX + x;
                int chunkZ = playerChunkZ + z;

                if (client.world.isChunkLoaded(chunkX, chunkZ)) {
                    WorldChunk chunk = client.world.getChunk(chunkX, chunkZ);
                    
                    if (hasMatureAmethyst(chunk)) {
                        drawChunkSquare(buffer, matrix4f, chunkX * 16, chunkZ * 16);
                    }
                }
            }
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrices.pop();
    }

    private static boolean hasMatureAmethyst(WorldChunk chunk) {
        int minSectionY = chunk.getBottomSectionCoord();
        int maxSectionY = chunk.getTopSectionCoord();

        for (int y = minSectionY; y <= maxSectionY; y++) {
            var section = chunk.getSectionArray()[ChunkSectionPos.getSectionCoordIndex(y)];
            if (section != null && !section.isEmpty()) {
                BlockPos minPos = new BlockPos(chunk.getPos().getStartX(), section.getYOffset(), chunk.getPos().getStartZ());
                for (int bx = 0; bx < 16; bx++) {
                    for (int by = 0; by < 16; by++) {
                        for (int bz = 0; bz < 16; bz++) {
                            BlockState state = section.getBlockState(bx, by, bz);
                            if (state.isOf(Blocks.AMETHYST_CLUSTER)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static void drawChunkSquare(BufferBuilder buffer, Matrix4f matrix, int startX, int startZ) {
        int yLevel = 64; // Höhe, auf der das Quadrat schwebt
        int size = 16;
        float r = 1.0F; // Pink / Magenta
        float g = 0.08F;
        float b = 0.58F;
        float a = 1.0F;

        buffer.vertex(matrix, startX, yLevel, startZ).color(r, g, b, a);
        buffer.vertex(matrix, startX + size, yLevel, startZ).color(r, g, b, a);
        
        buffer.vertex(matrix, startX + size, yLevel, startZ).color(r, g, b, a);
        buffer.vertex(matrix, startX + size, yLevel, startZ + size).color(r, g, b, a);
        
        buffer.vertex(matrix, startX + size, yLevel, startZ + size).color(r, g, b, a);
        buffer.vertex(matrix, startX, yLevel, startZ + size).color(r, g, b, a);
        
        buffer.vertex(matrix, startX, yLevel, startZ + size).color(r, g, b, a);
        buffer.vertex(matrix, startX, yLevel, startZ).color(r, g, b, a);
    }
}
