package com.villagerlock.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class EntityFreezeHelper {
	private static void rotateEntity(@NonNull Entity entity, @NonNull BlockPos pos) {
		BlockState state = entity.level().getBlockState(pos);
		Direction facing = Direction.NORTH;

		if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
			facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		} else if (state.hasProperty(BlockStateProperties.FACING)) {
			facing = state.getValue(BlockStateProperties.FACING);
		}

		float blockYaw = facing.toYRot();

		entity.setDeltaMovement(Vec3.ZERO);
		entity.snapTo(pos.getX() + 0.5, pos.getY() + 0.05, pos.getZ() + 0.5, blockYaw, 0.00f);

		if (entity instanceof LivingEntity living) {
			living.setYHeadRot(blockYaw);
			living.setYBodyRot(blockYaw);
		}
	}

	public static void freezeEntity(@NonNull Entity entity, BlockPos blockPos) {
		entity.setNoGravity(true);

		if (entity instanceof LivingEntity living) {
			var attribute = living.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
			if (attribute != null) {
				attribute.setBaseValue(1.0);
			}
		}

		if (entity instanceof Mob mobEntity) {
			mobEntity.getNavigation().stop();
			mobEntity.setZza(0);
			mobEntity.setXxa(0);
		}

		if (blockPos != null) {
			rotateEntity(entity, blockPos);
		}

		entity.needsSync = true;
	}

	public static void unfreezeEntity(@NonNull Entity entity, BlockPos blockPos) {
		entity.setNoGravity(false);

		if (entity instanceof LivingEntity living) {
			AttributeInstance attribute = living.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

			if (attribute != null) {
				attribute.setBaseValue(0.0);
			}

			if (blockPos != null) {
				living.teleportTo(blockPos.getX() + 0.5, blockPos.getY() + 0.2, blockPos.getZ() + 0.5);
				living.setDeltaMovement(0, 0, 0);
			}
		}
	}
}
