package com.trongthang.realistictorches;

import com.trongthang.realistictorches.managers.BlocksManager;
import com.trongthang.realistictorches.managers.TorchBurnManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class TorchBurnHandler {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(TorchBurnHandler::onEndServerTick);
    }

    private static void onEndServerTick(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {

            TorchBurnManager manager = TorchBurnManager.get(world);
            long currentTime = world.getTime();

            // Iterate over a copy of the entry set to avoid ConcurrentModificationException
            for (Map.Entry<BlockPos, Long> entry : new HashMap<>(manager.getBurnTimes()).entrySet()) {
                BlockPos pos = entry.getKey();
                BlockState state = world.getBlockState(pos);

                if(ModConfig.getInstance().rainExtinguishTorches) {
                    if(world.isRaining() || world.isThundering()){
                        if(world.isSkyVisible(pos) && world.getBiome(pos).value().getPrecipitation(pos) != Biome.Precipitation.NONE){
                            unlitTorch(state, world, pos);
                            manager.cancelBurn(pos);
                            continue;
                        }
                    }
                }

                long burnTime = entry.getValue();

                if (currentTime >= burnTime) {
                    unlitTorch(state, world, pos);
                    manager.cancelBurn(pos);
                }
            }
        }
    }

    private static void unlitTorch(BlockState state, World world, BlockPos pos){
        if(state.getBlock() == BlocksManager.UNLIT_TORCH || state.getBlock() == BlocksManager.UNLIT_WALL_TORCH) return;
        
        if (state.getBlock() instanceof WallTorchBlock) {
            BlockState unlitState = BlocksManager.UNLIT_WALL_TORCH.getDefaultState()
                    .with(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING));
            world.setBlockState(pos, unlitState);
            world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH,
                    SoundCategory.BLOCKS, 1.0F, 1.0F);
        } else if (state.getBlock() instanceof TorchBlock) {
            world.setBlockState(pos, BlocksManager.UNLIT_TORCH.getDefaultState());
            world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH,
                    SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }
}
