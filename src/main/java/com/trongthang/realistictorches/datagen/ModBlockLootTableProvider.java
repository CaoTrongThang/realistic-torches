package com.trongthang.realistictorches.datagen;

import com.trongthang.realistictorches.managers.BlocksManager;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(BlocksManager.UNLIT_WALL_TORCH, Items.STICK);
        addDrop(BlocksManager.UNLIT_TORCH, Items.STICK);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {

    }
}
