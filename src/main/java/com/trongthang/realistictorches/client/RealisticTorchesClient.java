package com.trongthang.realistictorches.client;

import com.trongthang.realistictorches.managers.BlocksManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import static com.trongthang.realistictorches.RealisticTorches.PLAY_BLOCK_LAVA_EXTINGUISH;

public class RealisticTorchesClient implements ClientModInitializer  {
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksManager.UNLIT_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksManager.UNLIT_WALL_TORCH, RenderLayer.getCutout());

        ClientPlayNetworking.registerGlobalReceiver(PLAY_BLOCK_LAVA_EXTINGUISH, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                // Play sound locally
                client.player.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.7f, 1f);
            });
        });
    }
}
