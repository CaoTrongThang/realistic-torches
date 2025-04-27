package com.trongthang.realistictorches;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

public class CustomTorchItem extends VerticallyAttachableBlockItem {
    public CustomTorchItem(Block standingBlock, Block wallBlock, Settings settings) {
        super(standingBlock, wallBlock, settings, Direction.DOWN);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        ActionResult actionResult = this.place(new ItemPlacementContext(context));
        if (!actionResult.isAccepted() && this.isFood()) {
            ActionResult actionResult2 = this.use(context.getWorld(), context.getPlayer(), context.getHand()).getResult();
            return actionResult2 == ActionResult.CONSUME ? ActionResult.CONSUME_PARTIAL : actionResult2;
        } else {
            return actionResult;
        }
    }

}
