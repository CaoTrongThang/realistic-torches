package com.trongthang.realistictorches.managers;

import com.trongthang.realistictorches.RealisticTorches;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TorchBurnManager extends PersistentState {
    private static final String NAME = "realistictorches_burn_data";
    private final ConcurrentHashMap<BlockPos, Long> burnTimes = new ConcurrentHashMap<>();

    public Map<BlockPos, Long> getBurnTimes() {
        return burnTimes;
    }

    // Updated get() using getOrCreate without a Type field.
    public static TorchBurnManager get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TorchBurnManager::fromNbt, TorchBurnManager::new, NAME);
    }

    public static TorchBurnManager fromNbt(NbtCompound nbt) {
        TorchBurnManager manager = new TorchBurnManager();
        NbtCompound times = nbt.getCompound("burnTimes");
        for (String key : times.getKeys()) {
            long posLong = Long.parseLong(key); // Key is the position's long string
            BlockPos pos = BlockPos.fromLong(posLong);
            long time = times.getLong(key); // Value is the burn time
            manager.burnTimes.put(pos, time);
        }
        return manager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound times = new NbtCompound();
        // Convert BlockPos to a long string for the key.
        burnTimes.forEach((pos, time) -> times.putLong(Long.toString(pos.asLong()), time));
        nbt.put("burnTimes", times);
        return nbt;
    }

    // Note: Because persistent state doesn’t store the world reference, pass the world in.
    public void scheduleBurn(ServerWorld world, BlockPos pos, long burnDuration) {
        long worldTime = world.getTime();
        burnTimes.put(pos, worldTime + burnDuration);

        markDirty();
    }

    public void cancelBurn(BlockPos pos) {
        burnTimes.remove(pos);
        markDirty();
    }
}
