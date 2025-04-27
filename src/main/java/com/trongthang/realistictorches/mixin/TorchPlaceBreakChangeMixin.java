package com.trongthang.realistictorches.mixin;

import com.trongthang.realistictorches.ModConfig;
import com.trongthang.realistictorches.RealisticTorches;
import com.trongthang.realistictorches.VerticalAttachmentAccessor;
import com.trongthang.realistictorches.managers.BlocksManager;
import com.trongthang.realistictorches.managers.TorchBurnManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class TorchPlaceBreakChangeMixin {
    @Inject(
            method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
            at = @At("HEAD")
    )
    private void onBlockStateChange(
            BlockPos pos,
            BlockState newState,
            int flags,
            int maxUpdateDepth,
            CallbackInfoReturnable<Boolean> cir
    ) {
        World world = (World) (Object) this;
        BlockState oldState = world.getBlockState(pos);
        if (!world.isClient() && oldState != newState) {
            TorchBurnManager manager;
            if ((newState.getBlock() instanceof TorchBlock || newState.getBlock() instanceof WallTorchBlock) && !world.isClient()) {

//                if(ModConfig.getInstance().allowPermanentTorches){
//                    String blockId = Registries.BLOCK.getId(newState.getBlock()).toString();
//                    Item item = Registries.ITEM.get(Registries.BLOCK.getId(newState.getBlock()));
//                    String blockIdWallVersion = Registries.BLOCK.getId(((VerticalAttachmentAccessor) item).getWallBlock().getDefaultState().getBlock()).toString();
//
//                    if(blockId != null || blockIdWallVersion != null){
//                        if(ModConfig.getInstance().permanentTorches.contains(blockId) || ModConfig.getInstance().permanentTorches.contains(blockIdWallVersion){
//                            return;
//                        }
//                    }
//                }

                manager = TorchBurnManager.get((ServerWorld)world);
                manager.scheduleBurn((ServerWorld)world, pos, (long)ModConfig.getInstance().torchBurnOutTime);
                return;
            }

            if ((oldState.getBlock() instanceof TorchBlock || oldState.getBlock() instanceof WallTorchBlock) && !world.isClient()) {
                manager = TorchBurnManager.get((ServerWorld)world);
                manager.cancelBurn(pos);
            }
        }
    }
}
