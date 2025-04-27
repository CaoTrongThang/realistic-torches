package com.trongthang.realistictorches;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<Item> TORCHES = createTags("torch_items");
    private static TagKey<Item> createTags(String name){
        return TagKey.of(RegistryKeys.ITEM, new Identifier(RealisticTorches.MOD_ID, name));
    }
}
