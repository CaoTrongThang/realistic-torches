package com.trongthang.realistictorches.managers;

import com.trongthang.realistictorches.RealisticTorches;
import com.trongthang.realistictorches.blocks.CustomTorch;
import com.trongthang.realistictorches.blocks.CustomWallTorch;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlocksManager {
    public static Block UNLIT_TORCH;
    public static Block UNLIT_WALL_TORCH;

    public static void registerModBlocks() {
        // Initialize blocks first
        UNLIT_TORCH = registerBlock("unlit_torch",
                new CustomTorch(FabricBlockSettings.create().noCollision().breakInstantly().luminance(0)));

        UNLIT_WALL_TORCH = registerBlock("unlit_wall_torch",
                new CustomWallTorch(FabricBlockSettings.create().noCollision().breakInstantly().luminance(0)));
    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(RealisticTorches.MOD_ID, name), block);
    }
}
