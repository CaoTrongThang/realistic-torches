package com.trongthang.realistictorches.blockentities;

import com.trongthang.realistictorches.RealisticTorches;
import com.trongthang.realistictorches.managers.BlocksEntitiesManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class LastTorchEntity extends BlockEntity {

    private String originalBlock = "";
    private Direction facingDirection;

    public LastTorchEntity(BlockPos pos, BlockState state) {
        super(BlocksEntitiesManager.UNLIT_TORCH_BLOCK_ENITITY, pos, state);
    }

    public String getOriginalBlock() {
        return this.originalBlock;
    }
    public Direction getFacingDirection() {
        return this.facingDirection;
    }

    public void setFacingDirection(Direction diretion) {
        facingDirection = diretion;
    }

    public void setOriginalBlock(String block) {
        this.originalBlock = block;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if(this.originalBlock != null){
            nbt.putString("originalTorch", originalBlock);
        }

        if (facingDirection != null) {
            nbt.putString("FacingDirection", facingDirection.getName());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if(nbt.contains("originalTorch")){
            this.originalBlock = nbt.getString("originalTorch");
        }

        if (nbt.contains("FacingDirection")) {
            facingDirection = Direction.byName(nbt.getString("FacingDirection"));
        }
    }
}
