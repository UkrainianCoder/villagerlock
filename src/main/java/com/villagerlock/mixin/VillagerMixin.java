package com.villagerlock.mixin;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerMixin {
	@Shadow
	@Final
	private static Logger LOGGER;

	@Unique
	private VillagerPostBlockEntity getVillagerPostEntity(VillagerEntity villager) {
		World world = villager.getEntityWorld();
		BlockPos pos = villager.getBlockPos();
		BlockEntity blockEntity = world.getBlockEntity(pos);

		if (blockEntity instanceof VillagerPostBlockEntity post && post.isOccupied() && post.getEntityUuid() == villager.getUuid()) {
			return post;
		}

		BlockPos belowPos = pos.down();
		BlockEntity belowBlockEntity = world.getBlockEntity(belowPos);
		if (belowBlockEntity instanceof VillagerPostBlockEntity post && post.isOccupied() && post.getEntityUuid() == villager.getUuid()) {
			return post;
		}

		return null;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		VillagerEntity villager = (VillagerEntity) (Object) this;
		VillagerPostBlockEntity post = getVillagerPostEntity(villager);

		if (post != null) {
			LOGGER.info("Villager post block entity tick -> {} <- {}", post.getEntityUuid(), villager.getUuid());
		}
	}
}
