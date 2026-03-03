package com.villagerlock.mixin;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import com.villagerlock.blocks.helpers.VillagerPostBlockHelper;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
	@Inject(method = "convertTo*", at = @At("HEAD"))
	private void onConverted(CallbackInfoReturnable<MobEntity> cir) {
		MobEntity oldEntity = (MobEntity) (Object) this;
		if (oldEntity.getEntityWorld().isClient()) {
			return;
		}

		if (oldEntity instanceof VillagerEntity oldVillager) {
			VillagerPostBlockEntity entity = VillagerPostBlockHelper.getVillagerPostEntity(oldVillager);
			if (entity != null && entity.isOccupied() && entity.getEntityUuid().equals(oldVillager.getUuid())) {
				entity.unseat(oldEntity.getEntityWorld(), false);
			}
		}

		if (oldEntity instanceof ZombieVillagerEntity oldZombieVillager) {
			VillagerPostBlockEntity entity = VillagerPostBlockHelper.getVillagerPostEntity(oldZombieVillager);
			if (entity != null && entity.isOccupied() && entity.getEntityUuid().equals(oldZombieVillager.getUuid())) {
				entity.unseat(oldEntity.getEntityWorld(), false);
			}
		}
	}
}