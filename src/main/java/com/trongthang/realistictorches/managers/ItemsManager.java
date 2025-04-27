package com.trongthang.realistictorches.managers;

import com.trongthang.realistictorches.CustomTorchItem;
import com.trongthang.realistictorches.RealisticTorches;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemsManager {
    public static Item UNLIT_TORCH;

    public static Item TORCH_ITEM;

    public static void register() {
        TORCH_ITEM = Registry.register(
                Registries.ITEM,
                new Identifier(RealisticTorches.MOD_ID, "unlit_torch"),
                new CustomTorchItem(
                        BlocksManager.UNLIT_TORCH,
                        BlocksManager.UNLIT_WALL_TORCH,
                        new FabricItemSettings()
                )
        );
    }

    private static Item registerItem(String name, Item item) {
        Item registeredItem = Registry.register(Registries.ITEM,
                new Identifier(RealisticTorches.MOD_ID, name), item);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(registeredItem);
        });

        return registeredItem;
    }
}
