package com.trongthang.realistictorches.blocks;

import com.trongthang.realistictorches.blockentities.LastTorchEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.trongthang.realistictorches.Utils.onUnlitTorchItemUse;

public class CustomWallTorch extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    // Adjusted voxel shapes to match vanilla proportions
    protected static final VoxelShape BIG_SHAPE_NORTH = Block.createCuboidShape(4.0, 2.0, 10.0, 12.0, 14.0, 16.0);
    protected static final VoxelShape BIG_SHAPE_SOUTH = Block.createCuboidShape(4.0, 2.0, 0.0, 12.0, 14.0, 6.0);
    protected static final VoxelShape BIG_SHAPE_EAST  = Block.createCuboidShape(0.0, 2.0, 4.0, 6.0, 14.0, 12.0);
    protected static final VoxelShape BIG_SHAPE_WEST  = Block.createCuboidShape(10.0, 2.0, 4.0, 16.0, 14.0, 12.0);

    public CustomWallTorch(Settings settings) {
        super(settings.nonOpaque().noCollision());
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return onUnlitTorchItemUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> BIG_SHAPE_NORTH;
            case SOUTH -> BIG_SHAPE_SOUTH;
            case EAST -> BIG_SHAPE_EAST;
            case WEST -> BIG_SHAPE_WEST;
            default -> BIG_SHAPE_NORTH;
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Vanilla-style placement logic
        Direction[] directions = ctx.getPlacementDirections();

        for (Direction dir : directions) {
            if (dir.getAxis().isHorizontal()) {
                Direction facingToSet = dir.getOpposite(); // Critical fix
                BlockPos attachedPos = ctx.getBlockPos().offset(dir);
                BlockState attachedState = ctx.getWorld().getBlockState(attachedPos);

                if (attachedState.isSolidBlock(ctx.getWorld(), attachedPos)) {
                    return getDefaultState().with(FACING, facingToSet);
                }
            }
        }

        return null; // Fallback if no valid position
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL; // Required for the block to render
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LastTorchEntity(pos, state);
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
}
