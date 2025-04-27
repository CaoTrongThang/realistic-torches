package com.trongthang.realistictorches;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.gen.Accessor;

public interface VerticalAttachmentAccessor {
    Block getWallBlock();
    Direction getVerticalAttachmentDirection();
}
