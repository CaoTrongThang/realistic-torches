package com.trongthang.realistictorches;

import com.trongthang.realistictorches.blockentities.LastTorchEntity;
import com.trongthang.realistictorches.managers.BlocksManager;
import com.trongthang.realistictorches.managers.ItemsManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class Utils {
    public static ItemStack createFromLitTorch(ItemStack litTorch) {
        ItemStack unlit = new ItemStack(ItemsManager.UNLIT_TORCH);
        NbtCompound nbt = new NbtCompound();
        nbt.putString("originalTorch", Registries.ITEM.getId(litTorch.getItem()).toString());
        unlit.setNbt(nbt);
        return unlit;
    }

    public static Optional<Item> getOriginalTorch(ItemStack stack) {
        if (!stack.hasNbt()) return Optional.empty();
        String torchId = stack.getNbt().getString("originalTorch");
        return Optional.ofNullable(Registries.ITEM.get(new Identifier(torchId)));
    }

    public static ActionResult onUnlitTorchItemUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);
        if (world.isClient) return ActionResult.PASS;

        if (heldItem.isOf(Items.FLINT_AND_STEEL)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof LastTorchEntity access) {
                Identifier originalId = new Identifier(access.getOriginalBlock());
                Block originalBlock = Registries.BLOCK.get(originalId);


                if (originalBlock == Blocks.AIR && (!originalBlock.getDefaultState().isOf(Blocks.WALL_TORCH) || !originalBlock.getDefaultState().isOf(Blocks.TORCH))) {
                    if (state.getBlock() == BlocksManager.UNLIT_WALL_TORCH) {
                        BlockState newState = Blocks.WALL_TORCH.getDefaultState()
                                .with(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING));
                        world.setBlockState(pos, newState);
                    } else {
                        BlockState newState = Blocks.TORCH.getDefaultState();
                        world.setBlockState(pos, newState);
                    }
                } else {
                    if (access.getFacingDirection() != null) {
                        BlockState defaultState = originalBlock.getDefaultState();
                        // Check if the default state contains a "facing" property
                        if (defaultState.contains(WallTorchBlock.FACING)) {
                            BlockState litTorchState = defaultState.with(WallTorchBlock.FACING, access.getFacingDirection());
                            world.setBlockState(pos, litTorchState);
                        } else {
                            // If the block does not support the facing property, simply set the default state
                            world.setBlockState(pos, defaultState);
                        }
                    } else {
                        if (state.getBlock() == BlocksManager.UNLIT_WALL_TORCH) {
                            Item item = Registries.ITEM.get(Registries.BLOCK.getId(originalBlock));
                            BlockState wallVersion = null;
                            if (item instanceof VerticallyAttachableBlockItem) {
                                // Use mixin or accessor to expose getBlockPair()
                                wallVersion = ((VerticalAttachmentAccessor) item).getWallBlock().getDefaultState().with(Properties.HORIZONTAL_FACING, (Direction) state.get(Properties.HORIZONTAL_FACING));
                            }
                            if (wallVersion != null) {
                                world.setBlockState(pos, wallVersion);
                            }
                        } else {
                            world.setBlockState(pos, originalBlock.getDefaultState());
                        }
                    }
                }

                if (!world.isClient()) {
                    if (!player.isCreative()) {
                        heldItem.damage(1, player, (p) -> {
                            p.sendToolBreakStatus(hand);
                        });
                    }
                    world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE,
                            SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
                    world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResult.SUCCESS;
            }
        } else if (heldItem.isIn(ModTags.TORCHES)) {

            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof LastTorchEntity access) {
                Identifier originalId = new Identifier(access.getOriginalBlock());
                Block originalBlock = Registries.BLOCK.get(originalId);

                if (originalBlock == Blocks.AIR || (!originalBlock.getDefaultState().isOf(Blocks.WALL_TORCH) || !originalBlock.getDefaultState().isOf(Blocks.TORCH))) {
                    if (state.getBlock() == BlocksManager.UNLIT_WALL_TORCH) {
                        BlockState newState = Blocks.WALL_TORCH.getDefaultState()
                                .with(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING));
                        world.setBlockState(pos, newState);
                    } else {
                        BlockState newState = Blocks.TORCH.getDefaultState();
                        world.setBlockState(pos, newState);
                    }
                } else {
                    if (access.getFacingDirection() != null) {
                        BlockState defaultState = originalBlock.getDefaultState();
                        // Check if the default state contains a "facing" property
                        if (defaultState.contains(WallTorchBlock.FACING)) {
                            BlockState litTorchState = defaultState.with(WallTorchBlock.FACING, access.getFacingDirection());
                            world.setBlockState(pos, litTorchState);
                        } else {
                            // If the block does not support the facing property, simply set the default state
                            world.setBlockState(pos, defaultState);
                        }
                    } else {
                        world.setBlockState(pos, originalBlock.getDefaultState());
                    }
                }

                if (!world.isClient()) {
                    if (!player.isCreative()) {
                        heldItem.decrement(1);
                    }

                    world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH,
                            SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
                    world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT,
                            SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResult.SUCCESS;

            }
        }
        return ActionResult.PASS;
    }
}

