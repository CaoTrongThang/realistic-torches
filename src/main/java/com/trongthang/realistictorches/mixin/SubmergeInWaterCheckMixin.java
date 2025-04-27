package com.trongthang.realistictorches.mixin;

import com.trongthang.realistictorches.InventoryHandler;
import com.trongthang.realistictorches.ModConfig;
import com.trongthang.realistictorches.RealisticTorches;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class) // Target PlayerEntity instead of ServerPlayerEntity
public abstract class SubmergeInWaterCheckMixin {
    @Unique
    private long lastWaterCheckTime = 0L;

    @Inject(method = "tick", at = @At("HEAD"))
    private void modifySubmersion(CallbackInfo ci) {
        if(!ModConfig.getInstance().submergeInWaterUnlitTorchesInInventory) return;

        PlayerEntity self = (PlayerEntity) (Object) this;

        if (self.isSubmergedIn(FluidTags.WATER)) {
            long currentTime = self.getWorld().getTime();
            if (currentTime - lastWaterCheckTime >= 20) {
                InventoryHandler.processInventory(self);
                lastWaterCheckTime = currentTime;
            }
        }
    }
}
