package com.villagerlock.mixin;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public class WanderingTraderEntityMixin {
	@Inject(method = "maybeDespawn", at = @At("HEAD"), cancellable = true)
	private void preventDespawn(CallbackInfo ci) {
		WanderingTrader trader = (WanderingTrader) (Object) this;

		if (VillagerPostBlockEntity.isEntityOnPost(trader)) {
			// Delay despawn is not calculating if canceled, so we don't need to set delay despawn here
			ci.cancel();
		}
	}
}