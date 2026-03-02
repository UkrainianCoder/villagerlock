package com.villagerlock.tasks;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Objects;

import static com.villagerlock.VillagerLock.LOGGER;

public final class AssignProfessionOnVillagePostTask extends MultiTickTask<VillagerEntity> {
	public AssignProfessionOnVillagePostTask() {
		super(Map.of());
	}

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

	@Override
	protected boolean shouldRun(ServerWorld world, VillagerEntity villager) {
		return getVillagerPostEntity(villager) != null && Objects.requireNonNull(villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE)).isEmpty();
	}

	@Override
	protected void run(ServerWorld world, VillagerEntity villager, long time) {
		VillagerPostBlockEntity post = getVillagerPostEntity(villager);
		if (post == null) {
			return;
		}

		LOGGER.info("run");
	}

	@Override
	protected void keepRunning(ServerWorld world, VillagerEntity entity, long time) {
	}

	private boolean isValidProfessionPOI(RegistryEntry<PointOfInterestType> poiType) {
		for (VillagerProfession profession : Registries.VILLAGER_PROFESSION) {
			if (profession.heldWorkstation().test(poiType)) {
				return true;
			}
		}

		return false;
	}

	private boolean isInsideRange(BlockPos center, BlockPos pos) {
		return Math.abs(center.getX() - pos.getX()) <= 1 && Math.abs(center.getZ() - pos.getZ()) <= 1;
	}
}