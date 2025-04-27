package com.trongthang.realistictorches;

import com.trongthang.realistictorches.managers.BlocksEntitiesManager;
import com.trongthang.realistictorches.managers.BlocksManager;
import com.trongthang.realistictorches.managers.ItemsManager;
import com.trongthang.realistictorches.managers.TorchBurnManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RealisticTorches implements ModInitializer {
	public static final String MOD_ID = "realistictorches";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier PLAY_BLOCK_LAVA_EXTINGUISH = new Identifier(MOD_ID, "play_block_lava_extinguish");

	@Override
	public void onInitialize() {
		ModConfig.loadConfig();
		LOGGER.info(MOD_ID + " has been initialized!");

		TorchBurnHandler.register();
		TorchRelightHandler.register();

		BlocksManager.registerModBlocks();
		BlocksEntitiesManager.initialize();

		ItemsManager.register();

		ServerLifecycleEvents.SERVER_STARTING.register(RealisticTorches::cleanTorch);
		ServerLifecycleEvents.SERVER_STOPPING.register(RealisticTorches::cleanTorch);

//		ServerLifecycleEvents.SERVER_STARTED.register((server) ->{
//			Registry<Item> itemRegistry = server.getRegistryManager().get(RegistryKeys.ITEM);
//
//			for(Item i : itemRegistry){
//				if(itemRegistry.getId(i).getNamespace().equals("more_beautiful_torches") && !itemRegistry.getId(i).toString().contains("redstone")){
//					LOGGER.info("\"" + itemRegistry.getId(i)  + "\",");
//				}
//			}
//		});
	}

	private static void cleanTorch(MinecraftServer server) {
		for (ServerWorld world : server.getWorlds()) {

			TorchBurnManager manager = TorchBurnManager.get(world);
			long currentTime = world.getTime();

			for (Map.Entry<BlockPos, Long> entry : new HashMap<>(manager.getBurnTimes()).entrySet()) {
				BlockPos pos = entry.getKey();
				long burnTime = entry.getValue();
				BlockState state = world.getBlockState(pos);
				if (currentTime >= burnTime) {
					manager.cancelBurn(pos);
				}

				if (!(state.getBlock() instanceof WallTorchBlock) && !(state.getBlock() instanceof TorchBlock)) {
					manager.cancelBurn(pos);
				}
			}
		}
	}

}
