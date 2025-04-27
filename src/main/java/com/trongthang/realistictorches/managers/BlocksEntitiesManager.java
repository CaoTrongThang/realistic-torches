package com.trongthang.realistictorches.managers;

import com.trongthang.realistictorches.RealisticTorches;
import com.trongthang.realistictorches.blockentities.LastTorchEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlocksEntitiesManager {

    // The BlockEntityType that will hold the custom block entity
    public static final BlockEntityType<LastTorchEntity> UNLIT_TORCH_BLOCK_ENITITY =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(RealisticTorches.MOD_ID, "unlit_torch_block_entity"),
                    BlockEntityType.Builder.create(LastTorchEntity::new, BlocksManager.UNLIT_TORCH).build(null)
            );
    
    public static void initialize() {
        RealisticTorches.LOGGER.info("Registering Block Entities...");
    }
}

