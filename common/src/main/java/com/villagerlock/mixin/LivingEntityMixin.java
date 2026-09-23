package com.villagerlock.mixin;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import com.villagerlock.blocks.helpers.VillagerPostBlockHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "die", at = @At("HEAD"))
	private void onEntityDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (VillagerPostBlockHelper.isEntityOnPost(entity)) {
			VillagerPostBlockEntity postEntity = VillagerPostBlockHelper.getPostEntity(entity);
			if (postEntity != null && postEntity.isOccupied() && postEntity.getEntityUuid().equals(entity.getUUID())) {
				postEntity.unseat(entity.level(), false);
			}
		}
	}
}