package com.trongthang.realistictorches.mixin;

import com.trongthang.realistictorches.ModConfig;
import com.trongthang.realistictorches.RealisticTorches;
import com.trongthang.realistictorches.blockentities.LastTorchEntity;
import com.trongthang.realistictorches.managers.BlocksManager;
import com.trongthang.realistictorches.managers.TorchBurnManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class BlockStateChangeMixin {
    @Inject(
            method = "onStateReplaced",
            at = @At("HEAD")
    )
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        if (newState.hasBlockEntity()) {
            if (state.getBlock() != Blocks.AIR) {
                if(newState.getBlock() == BlocksManager.UNLIT_TORCH || newState.getBlock() == BlocksManager.UNLIT_WALL_TORCH && !world.isClient){
                    if (world instanceof ServerWorld) {
                        ServerWorld serverWorld = (ServerWorld)world;
                        BlockEntity blockEntity = serverWorld.getBlockEntity(pos);

                        if (blockEntity instanceof LastTorchEntity) {
                            LastTorchEntity access = (LastTorchEntity)blockEntity;
                            Identifier originalId = Registries.BLOCK.getId(state.getBlock());

                            access.setOriginalBlock(originalId.toString());
                            if (newState.getBlock() == BlocksManager.UNLIT_WALL_TORCH) {
                                access.setFacingDirection((Direction)state.get(Properties.HORIZONTAL_FACING));
                            }
                        }
                    }
                }
            }
        }
    }
}
