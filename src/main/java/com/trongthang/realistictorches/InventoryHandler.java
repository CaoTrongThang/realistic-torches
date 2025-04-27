package com.trongthang.realistictorches;

import com.trongthang.realistictorches.managers.ItemsManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class InventoryHandler {
    public static void processInventory(PlayerEntity player) {
        ItemStack[] inventory = player.getInventory().main.toArray(new ItemStack[0]);
        boolean isLitTorch = false;

        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack.isEmpty()) continue;

            isLitTorch = isLitTorch(stack);
            if (isLitTorch) {
                ItemStack unlit = createUnlitVariant(stack);
                unlit.setCount(stack.getCount());
                player.getInventory().setStack(i, unlit);
            }
        }

        ItemStack offhand =  player.getInventory().offHand.get(0);

        isLitTorch = isLitTorch(offhand);
        if (isLitTorch) {
            ItemStack unlit = createUnlitVariant(offhand);
            unlit.setCount(offhand.getCount());
            player.getInventory().offHand.set(0, unlit);
        }
    }

    private static boolean isLitTorch(ItemStack stack) {

        if (stack.isOf(ItemsManager.UNLIT_TORCH))
        {
            return false;
        }
        
        return stack.isIn(ModTags.TORCHES);
    }

    private static ItemStack createUnlitVariant(ItemStack original) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("originalTorch",
                Registries.ITEM.getId(original.getItem()).toString());

        ItemStack unlit = new ItemStack(ItemsManager.TORCH_ITEM);
        unlit.setNbt(nbt);
        return unlit;
    }
}
