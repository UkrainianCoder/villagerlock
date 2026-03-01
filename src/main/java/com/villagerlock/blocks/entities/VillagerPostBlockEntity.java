package com.villagerlock.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.villagerlock.ModBlocks.VILLAGER_POST_ENTITY;

public class VillagerPostBlockEntity extends BlockEntity {
	private int entityId = -1;
	private int seatId = -1;

	public VillagerPostBlockEntity(BlockPos pos, BlockState state) {
		super(VILLAGER_POST_ENTITY, pos, state);
	}

	public static void onTick(World world, BlockPos pos, BlockState state, VillagerPostBlockEntity blockEntity) {
		if (blockEntity.isOccupied()) {
			boolean kill = false;
			Entity rider = world.getEntityById(blockEntity.getEntityId());
			Entity seat = world.getEntityById(blockEntity.getSeatId());

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
		return entityId != -1 && seatId != -1;
	}

	public int getEntityId() {
		return entityId;
	}

	public int getSeatId() {
		return seatId;
	}

	public void seat(World world, Entity entity) {
		ArmorStandEntity seat = new ArmorStandEntity(world, pos.getX() + 0.5, pos.getY() - 2, pos.getZ() + 0.5);
		seat.setInvisible(true);
		seat.setNoGravity(true);
		world.spawnEntity(seat);
		entity.startRiding(seat);
		this.entityId = entity.getId();
		this.seatId = seat.getId();
	}

	public void unseat(World world) {
		if (seatId != -1 && entityId != -1) {
			Entity seat = world.getEntityById(seatId);
			Entity rider = world.getEntityById(entityId);

			if (seat != null) {
				seat.remove(Entity.RemovalReason.DISCARDED);
			}

			if (rider != null && rider.getVehicle() == seat) {
				rider.stopRiding();
			}

			this.entityId = -1;
			this.seatId = -1;
		}
	}
}

