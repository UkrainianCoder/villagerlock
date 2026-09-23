package com.villagerlock.blocks.entities;

import com.villagerlock.helpers.EntityFreezeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

import static com.villagerlock.ModBlocks.VILLAGER_POST_ENTITY;
import static com.villagerlock.VillagerLock.LOGGER;

public class VillagerPostBlockEntity extends BlockEntity {
	private UUID _entityUuid = null;

	public VillagerPostBlockEntity(BlockPos pos, BlockState state) {
		super(VILLAGER_POST_ENTITY.get(), pos, state);
	}

	public boolean isOccupied() {
		return _entityUuid != null;
	}

	public UUID getEntityUuid() {
		return _entityUuid;
	}

	public Entity getEntity(Level world) {
		if (world == null) {
			return getEntity(level);
		}

		if (isOccupied()) {
			return world.getEntity(_entityUuid);
		}

		return null;
	}

	public void seat(@NonNull Level world, @NonNull Entity entity) {
		if (!isOccupied() && !world.hasNeighborSignal(worldPosition)) {
			_entityUuid = entity.getUUID();
			entity.addTag("locked_on_post");
			EntityFreezeHelper.freezeEntity(entity, worldPosition);
			world.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			LOGGER.info("Seat entity {} on post block {}", entity, worldPosition);
		}
	}

	public void unseat(@NonNull Level world, boolean teleportToFreeBlock) {
		if (isOccupied()) {
			Entity rider = this.getEntity(world);

			if (rider != null) {
				rider.removeTag("locked_on_post");
				EntityFreezeHelper.unfreezeEntity(rider, teleportToFreeBlock ? worldPosition : null);
				LOGGER.info("Unseat entity {} on post block {}", rider, worldPosition);
			} else {
				LOGGER.warn(
						"Unseat entity with UUID {} on post block {} but entity not found in world",
						_entityUuid,
						worldPosition
				);
			}

			_entityUuid = null;
			setChanged();
			world.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

	@Override
	public void preRemoveSideEffects(@NonNull BlockPos pos, @NonNull BlockState state) {
		super.preRemoveSideEffects(pos, state);

		if (this.level != null) {
			unseat(this.level, true);
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
		return saveCustomOnly(registries);
	}

	@Override
	protected void loadAdditional(@NonNull ValueInput view) {
		super.loadAdditional(view);

		String entityUuidStr = view.getStringOr("EntityUuid", "");
		if (!entityUuidStr.isEmpty()) {
			try {
				_entityUuid = UUID.fromString(entityUuidStr);
			} catch (IllegalArgumentException e) {
				_entityUuid = null;
			}
		} else {
			_entityUuid = null;
		}
	}

	@Override
	protected void saveAdditional(@NonNull ValueOutput view) {
		super.saveAdditional(view);

		if (isOccupied() && _entityUuid != null) {
			view.putString("EntityUuid", _entityUuid.toString());
		}
	}
}
