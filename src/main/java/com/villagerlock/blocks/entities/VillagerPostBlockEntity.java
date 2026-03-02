package com.villagerlock.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

import static com.villagerlock.ModBlocks.VILLAGER_POST_ENTITY;
import static com.villagerlock.VillagerLock.LOGGER;

public class VillagerPostBlockEntity extends BlockEntity {
	private UUID _entityUuid = null;

	public VillagerPostBlockEntity(BlockPos pos, BlockState state) {
		super(VILLAGER_POST_ENTITY, pos, state);
	}

	public boolean isOccupied() {
		return _entityUuid != null;
	}

	public UUID getEntityUuid() {
		return _entityUuid;
	}

	public @Nullable Entity getEntity(World world) {
		if (isOccupied()) {
			Entity rider = world.getEntity(_entityUuid);
			if (rider == null || !rider.isAlive()) {
				unseat(world);
				return null;
			}

			return rider;
		}

		return null;
	}

	private void freezeEntity(Entity entity) {
		BlockState state = entity.getEntityWorld().getBlockState(pos);
		Direction facing = state.contains(Properties.HORIZONTAL_FACING) ? state.get(Properties.HORIZONTAL_FACING) : Direction.NORTH;
		float blockYaw = facing.getPositiveHorizontalDegrees();

		entity.setNoGravity(true);

		if (entity instanceof LivingEntity living) {
			var attribute = living.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE);
			if (attribute != null) {
				attribute.setBaseValue(1.0);
			}
		}

		if (entity instanceof MobEntity mobEntity) {
			mobEntity.setForwardSpeed(0);
			mobEntity.setSidewaysSpeed(0);
		}

		entity.setVelocity(Vec3d.ZERO);
		entity.refreshPositionAndAngles(
				pos.getX() + 0.5,
				pos.getY() + 0.05,
				pos.getZ() + 0.5,
				blockYaw,
				0.00f
		);

		if (entity instanceof LivingEntity living) {
			living.setHeadYaw(blockYaw);
			living.setBodyYaw(blockYaw);
		}

		entity.velocityDirty = true;
	}

	private void unfreezeEntity(Entity entity) {
		entity.setNoGravity(false);

		if (entity instanceof LivingEntity living) {
			var attribute = living.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE);
			if (attribute != null) {
				attribute.setBaseValue(0.0);
			}

			living.teleport(pos.getX() + 1, pos.getY(), pos.getZ() + 1, false);
		}
	}

	public void seat(World world, Entity entity) {
		if (!isOccupied() && !world.isReceivingRedstonePower(pos)) {
			_entityUuid = entity.getUuid();
			freezeEntity(entity);
			markDirty();
			LOGGER.info("Seated {} on {}", _entityUuid, pos);
		}
	}

	public void unseat(World world) {
		if (isOccupied()) {
			LOGGER.info("Unseated {} on {}", _entityUuid, pos);

			Entity rider = world.getEntity(_entityUuid);

			if (rider != null) {
				unfreezeEntity(rider);
			}

			_entityUuid = null;
			markDirty();
		}
	}

	@Override
	protected void readData(ReadView view) {
		super.readData(view);

		String entityUuidStr = view.getString("EntityUuid", "");
		if (!entityUuidStr.isEmpty()) {
			_entityUuid = UUID.fromString(entityUuidStr);
		}
	}

	@Override
	protected void writeData(WriteView view) {
		super.writeData(view);

		if (isOccupied()) {
			view.putString("EntityUuid", _entityUuid.toString());
			return;
		}

		view.putString("EntityUuid", "");
	}
}

