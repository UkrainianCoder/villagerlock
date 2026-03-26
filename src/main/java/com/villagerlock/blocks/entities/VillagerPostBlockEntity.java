package com.villagerlock.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

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
		if (entity.isNoGravity()) {
			return true;
		}

		return entity.entityTags().contains("locked_on_post");
	}

	public boolean isOccupied() {
		return _entityUuid != null;
	}

	public UUID getEntityUuid() {
		return _entityUuid;
	}

	@SuppressWarnings("resource")
	private void freezeEntity(Entity entity) {
		BlockState state = entity.level().getBlockState(worldPosition);
		Direction facing = state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ? state.getValue(BlockStateProperties.HORIZONTAL_FACING) : Direction.NORTH;
		float blockYaw = facing.toYRot();

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

		entity.setDeltaMovement(Vec3.ZERO);
		entity.snapTo(
				worldPosition.getX() + 0.5,
				worldPosition.getY() + 0.05,
				worldPosition.getZ() + 0.5,
				blockYaw,
				0.00f
		);

		if (entity instanceof LivingEntity living) {
			living.setYHeadRot(blockYaw);
			living.setYBodyRot(blockYaw);
		}

		entity.needsSync = true;
	}

	private void unfreezeEntity(Entity entity, boolean spawnAboveBlock) {
		entity.setNoGravity(false);

		if (entity instanceof LivingEntity living) {
			AttributeInstance attribute = living.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

			if (attribute != null) {
				attribute.setBaseValue(0.0);
			}

			if (spawnAboveBlock) {
				double spawnX = worldPosition.getX() + 0.5;
				double spawnY = worldPosition.getY() + 0.2;
				double spawnZ = worldPosition.getZ() + 0.5;
				living.teleportTo(spawnX, spawnY, spawnZ);
				living.setDeltaMovement(0, 0, 0);
			}
		}
	}

	public void seat(Level world, Entity entity) {
		if (!isOccupied() && !world.hasNeighborSignal(worldPosition)) {
			_entityUuid = entity.getUUID();
			entity.addTag("locked_on_post");
			freezeEntity(entity);
			setChanged();
			world.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			LOGGER.info("Seat entity {} on post block {}", _entityUuid, worldPosition);
		}
	}

	public void unseat(Level world, boolean teleportToFreeBlock) {
		if (isOccupied()) {
			LOGGER.info("Unseat entity {} on post block {}", _entityUuid, worldPosition);

			Entity rider = world.getEntity(_entityUuid);

			if (rider != null) {
				rider.removeTag("locked_on_post");
				unfreezeEntity(rider, teleportToFreeBlock);
			}

			_entityUuid = null;
			setChanged();
			world.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

	@Override
	protected void loadAdditional(@NonNull ValueInput view) {
		super.loadAdditional(view);

		String entityUuidStr = view.getStringOr("EntityUuid", "");
		if (!entityUuidStr.isEmpty()) {
			_entityUuid = UUID.fromString(entityUuidStr);
		}
	}

	@Override
	protected void saveAdditional(@NonNull ValueOutput view) {
		super.saveAdditional(view);

		if (isOccupied()) {
			view.putString("EntityUuid", _entityUuid.toString());
			return;
		}

		view.putString("EntityUuid", "");
	}
}