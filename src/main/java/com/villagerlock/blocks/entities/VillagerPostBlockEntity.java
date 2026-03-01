package com.villagerlock.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

import static com.villagerlock.ModBlocks.VILLAGER_POST_ENTITY;

public class VillagerPostBlockEntity extends BlockEntity {
	private UUID _entityUuid = null;
	private UUID _seatUuid = null;

	public VillagerPostBlockEntity(BlockPos pos, BlockState state) {
		super(VILLAGER_POST_ENTITY, pos, state);
	}

	public static void onTick(World world, VillagerPostBlockEntity blockEntity) {
		if (blockEntity.isOccupied()) {
			boolean kill = false;
			Entity rider = world.getEntity(blockEntity.getEntityUuid());
			Entity seat = world.getEntity(blockEntity.getSeatUuid());

			if (rider == null || rider.getVehicle() != seat) {
				kill = true;
			}

			if (seat == null) {
				kill = true;
			}

			if (kill) {
				blockEntity.unseat(world);
			}
		}
	}

	public boolean isOccupied() {
		return _entityUuid != null && _seatUuid != null;
	}

	public UUID getEntityUuid() {
		return _entityUuid;
	}

	public UUID getSeatUuid() {
		return _seatUuid;
	}

	public void seat(World world, Entity entity) {
		ArmorStandEntity seat = new ArmorStandEntity(world, pos.getX() + 0.5, pos.getY() - 2, pos.getZ() + 0.5);
		seat.setInvisible(true);
		seat.setNoGravity(true);
		world.spawnEntity(seat);
		entity.startRiding(seat);

		this._entityUuid = entity.getUuid();
		this._seatUuid = seat.getUuid();
	}

	public void unseat(World world) {
		if (this.isOccupied()) {
			Entity seat = world.getEntity(_seatUuid);
			Entity rider = world.getEntity(_entityUuid);

			if (seat != null) {
				seat.remove(Entity.RemovalReason.DISCARDED);
			}

			if (rider != null && rider.getVehicle() == seat) {
				rider.stopRiding();
			}

			this._entityUuid = null;
			this._seatUuid = null;
		}
	}

	@Override
	protected void readData(ReadView view) {
		super.readData(view);

		String entityUuidStr = view.getString("EntityUuid", "");
		String seatUuidStr = view.getString("SeatUuid", "");

		if (!entityUuidStr.isEmpty() && !seatUuidStr.isEmpty()) {
			this._entityUuid = UUID.fromString(entityUuidStr);
			this._seatUuid = UUID.fromString(seatUuidStr);
		}
	}

	@Override
	protected void writeData(WriteView view) {
		super.writeData(view);

		if (this.isOccupied()) {
			view.putString("EntityUuid", _entityUuid.toString());
			view.putString("SeatUuid", _seatUuid.toString());
		} else {
			view.putString("EntityUuid", "");
			view.putString("SeatUuid", "");
		}
	}
}

