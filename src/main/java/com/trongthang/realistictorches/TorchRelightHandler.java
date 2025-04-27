package com.trongthang.realistictorches;

import com.trongthang.realistictorches.managers.ItemsManager;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TorchRelightHandler {
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockState state = world.getBlockState(hitResult.getBlockPos());
            ItemStack stack = player.getStackInHand(hand);

            if ((state.isOf(Blocks.CAMPFIRE) || state.isOf(Blocks.FIRE)) && stack.isOf(ItemsManager.TORCH_ITEM)) {
                return tryRelightTorch(player, world, hand, stack, hitResult.getBlockPos());
            }
            return ActionResult.PASS;
        });
    }

    private static ActionResult tryRelightTorch(PlayerEntity player, World world, Hand hand,
                                                ItemStack stack, BlockPos pos) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains("originalTorch")) {
            return ActionResult.FAIL;
        }
        Identifier torchId = new Identifier(nbt.getString("originalTorch"));
        Item torchItem = Registries.ITEM.get(torchId);

        if (torchItem != Items.AIR) {
            ItemStack stackInHand = player.getStackInHand(hand);
            ItemStack newStack = new ItemStack(torchItem);
            newStack.setCount(stackInHand.getCount());
            player.setStackInHand(hand, newStack);
            world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH,
                    SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResult.SUCCESS;
        }

        ItemStack stackInHand = player.getStackInHand(hand);
        ItemStack newStack = new ItemStack(Items.TORCH);
        newStack.setCount(stackInHand.getCount());
        player.setStackInHand(hand, newStack);
        world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH,
                SoundCategory.BLOCKS, 1.0F, 1.0F);

        return ActionResult.SUCCESS;
    }
}
