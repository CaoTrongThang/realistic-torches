package com.trongthang.realistictorches.mixin;

import com.trongthang.realistictorches.VerticalAttachmentAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VerticallyAttachableBlockItem.class)
public abstract class MixinVerticallyAttachableBlockItem implements VerticalAttachmentAccessor {
    @Accessor("wallBlock")
    public abstract Block getWallBlock();

    @Accessor("verticalAttachmentDirection")
    public abstract Direction getVerticalAttachmentDirection();
}
