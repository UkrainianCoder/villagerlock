package com.villagerlock.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

import static com.villagerlock.ModBlocks.VILLAGER_POST_ENTITY;
import static com.villagerlock.VillagerLock.LOGGER;

public class VillagerPostBlockEntity extends BlockEntity {
	private static final int MAX_RADIUS = 5;

	private UUID _entityUuid = null;

	public VillagerPostBlockEntity(BlockPos pos, BlockState state) {
		super(VILLAGER_POST_ENTITY, pos, state);
	}

	public static boolean isEntityOnPost(Entity entity) {
		if (entity.hasNoGravity()) {
			return true;
		}

		return entity.getCommandTags().contains("locked_on_post");
	}

	public boolean isOccupied() {
		return _entityUuid != null;
	}

	public UUID getEntityUuid() {
		return _entityUuid;
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
			mobEntity.getNavigation().stop();
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

	private void unfreezeEntity(Entity entity, boolean teleportToFreeBlock) {
		entity.setNoGravity(false);

		if (entity instanceof LivingEntity living) {
			BlockPos startPos = living.getBlockPos();
			World world = living.getEntityWorld();
			EntityAttributeInstance attribute = living.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE);

			if (attribute != null) {
				attribute.setBaseValue(0.0);
			}

			for (int r = 0; r <= MAX_RADIUS; r++) {
				for (int dy = -r; dy <= r; dy++) {
					for (int dx = -r; dx <= r; dx++) {
						for (int dz = -r; dz <= r; dz++) {
							if (Math.abs(dx) != r && Math.abs(dy) != r && Math.abs(dz) != r && r != 0) {
								continue;
							}

							BlockPos targetPos = startPos.add(dx, dy, dz);
							if (world.isAir(targetPos) && world.isAir(targetPos.up()) && world.getBlockState(targetPos.down()).isSolidBlock(world, targetPos.down())) {
								double finalX = targetPos.getX() + 0.5;
								double finalY = targetPos.getY();
								double finalZ = targetPos.getZ() + 0.5;
								living.requestTeleport(finalX, finalY, finalZ);
								living.setVelocity(0, 0, 0);
								return;
							}
						}
					}
				}
			}
		}
	}

	public void seat(World world, Entity entity) {
		if (!isOccupied() && !world.isReceivingRedstonePower(pos)) {
			_entityUuid = entity.getUuid();
			entity.addCommandTag("locked_on_post");
			freezeEntity(entity);
			markDirty();
			world.updateListeners(pos, getCachedState(), getCachedState(), 3);
			LOGGER.info("Seat entity {} on post block {}", _entityUuid, pos);
		}
	}

	public void unseat(World world, boolean teleportToFreeBlock) {
		if (isOccupied()) {
			LOGGER.info("Unseat entity {} on post block {}", _entityUuid, pos);

			Entity rider = world.getEntity(_entityUuid);
			if (rider != null) {
				rider.removeCommandTag("locked_on_post");
				unfreezeEntity(rider, teleportToFreeBlock);
			}

			_entityUuid = null;
			markDirty();
			world.updateListeners(pos, getCachedState(), getCachedState(), 3);
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

