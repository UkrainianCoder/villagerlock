package com.villagerlock.mixin;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import com.villagerlock.blocks.helpers.VillagerPostBlockHelper;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobEntityMixin {
	@Inject(method = "convertTo*", at = @At("HEAD"))
	private void onConverted(CallbackInfoReturnable<Mob> cir) {
		Mob oldEntity = (Mob) (Object) this;
		if (oldEntity.level().isClientSide()) {
			return;
		}

		if (oldEntity instanceof Villager oldVillager) {
			VillagerPostBlockEntity entity = VillagerPostBlockHelper.getVillagerPostEntity(oldVillager);
			if (entity != null && entity.isOccupied() && entity.getEntityUuid().equals(oldVillager.getUUID())) {
				entity.unseat(oldEntity.level(), false);
			}
		}

		if (oldEntity instanceof ZombieVillager oldZombieVillager) {
			VillagerPostBlockEntity entity = VillagerPostBlockHelper.getVillagerPostEntity(oldZombieVillager);
			if (entity != null && entity.isOccupied() && entity.getEntityUuid().equals(oldZombieVillager.getUUID())) {
				entity.unseat(oldEntity.level(), false);
			}
		}
	}
}