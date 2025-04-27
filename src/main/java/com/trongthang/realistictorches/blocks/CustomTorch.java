package com.trongthang.realistictorches.blocks;

import com.trongthang.realistictorches.blockentities.LastTorchEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.trongthang.realistictorches.Utils.onUnlitTorchItemUse;

public class CustomTorch extends TorchBlock implements BlockEntityProvider {

    public CustomTorch(Settings settings) {
        super(settings, ParticleTypes.SMOKE);
    }
    protected static final VoxelShape LARGER_BOUNDING_SHAPE = Block.createCuboidShape(4.0F, 0.0F, 4.0F, 12.0F, 12.0F, 12.0F);

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return onUnlitTorchItemUse(state, world, pos, player, hand, hit);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return LARGER_BOUNDING_SHAPE;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (world.isClient) return;
        NbtCompound nbt = itemStack.getNbt();

        if (nbt != null && nbt.contains("originalTorch")) {
            LastTorchEntity access =  (LastTorchEntity) world.getBlockEntity(pos);
            access.setOriginalBlock(nbt.getString("originalTorch"));
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LastTorchEntity(pos, state);
    }
}
