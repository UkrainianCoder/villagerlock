package com.villagerlock.mixin;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(VillagerEntity.class)
public class VillagerMixin {
	@Unique
	private static final Map<Block, RegistryKey<VillagerProfession>> BLOCK_PROFESSION_MAP = Map.ofEntries(
			Map.entry(Blocks.SMOKER, VillagerProfession.BUTCHER),
			Map.entry(Blocks.BREWING_STAND, VillagerProfession.CLERIC),
			Map.entry(Blocks.BARREL, VillagerProfession.FISHERMAN),
			Map.entry(Blocks.CAULDRON, VillagerProfession.LEATHERWORKER),
			Map.entry(Blocks.STONECUTTER, VillagerProfession.MASON),
			Map.entry(Blocks.SMITHING_TABLE, VillagerProfession.TOOLSMITH),
			Map.entry(Blocks.BLAST_FURNACE, VillagerProfession.ARMORER),
			Map.entry(Blocks.CARTOGRAPHY_TABLE, VillagerProfession.CARTOGRAPHER),
			Map.entry(Blocks.COMPOSTER, VillagerProfession.FARMER),
			Map.entry(Blocks.FLETCHING_TABLE, VillagerProfession.FLETCHER),
			Map.entry(Blocks.LECTERN, VillagerProfession.LIBRARIAN),
			Map.entry(Blocks.LOOM, VillagerProfession.SHEPHERD),
			Map.entry(Blocks.GRINDSTONE, VillagerProfession.WEAPONSMITH)
	);

	@Unique
	private static RegistryKey<VillagerProfession> getProfessionByBlock(Block block) {
		if (block == null) {
			return VillagerProfession.NONE;
		}

		return BLOCK_PROFESSION_MAP.getOrDefault(block, VillagerProfession.NONE);
	}

	@Unique
	private static VillagerPostBlockEntity getVillagerPostEntity(VillagerEntity villager) {
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

	@Unique
	private static Object[] findProfessionBlock(ServerWorld world, VillagerEntity villager) {
		BlockPos pos = villager.getBlockPos();
		BlockPos[] adjacentPositions = new BlockPos[]{
				pos.north(),
				pos.south(),
				pos.east(),
				pos.west()
		};

		for (BlockPos adjacent : adjacentPositions) {
			Block adjacentBlock = world.getBlockState(adjacent).getBlock();
			if (BLOCK_PROFESSION_MAP.containsKey(adjacentBlock)) {
				return new Object[]{adjacent, adjacentBlock};
			}
		}

		return null;
	}

	@Unique
	private static void tryClaimProfession(ServerWorld world, VillagerEntity villager, RegistryEntry<VillagerProfession> profession, BlockPos professionPos) {
		Brain<VillagerEntity> brain = villager.getBrain();
		brain.forget(MemoryModuleType.JOB_SITE);
		brain.forget(MemoryModuleType.POTENTIAL_JOB_SITE);

		GlobalPos globalPos = GlobalPos.create(world.getRegistryKey(), professionPos);
		world.getRegistryManager().getOrThrow(RegistryKeys.POINT_OF_INTEREST_TYPE)
				.streamEntries()
				.findFirst()
				.ifPresent(poiType -> {
					PointOfInterestStorage poiStorage = world.getPointOfInterestStorage();
					if (poiStorage.getType(professionPos).isEmpty()) {
						poiStorage.add(professionPos, poiType);
					}

					brain.remember(MemoryModuleType.POTENTIAL_JOB_SITE, globalPos);
					brain.remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(professionPos));
				});
	}

	@Unique
	private static void tryReClaimProfession(ServerWorld world, VillagerEntity villager, RegistryEntry<VillagerProfession> profession, BlockPos professionPos) {
		Brain<VillagerEntity> brain = villager.getBrain();
		GlobalPos globalPos = GlobalPos.create(world.getRegistryKey(), professionPos);
		brain.remember(MemoryModuleType.JOB_SITE, globalPos);
	}

	@Unique
	private static void tryRemoveProfession(ServerWorld world, VillagerEntity villager) {
		RegistryEntry<VillagerProfession> professionEntry = Registries.VILLAGER_PROFESSION.getOrThrow(VillagerProfession.NONE);
		Brain<VillagerEntity> brain = villager.getBrain();
		brain.forget(MemoryModuleType.JOB_SITE);
		brain.forget(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
		brain.forget(MemoryModuleType.POTENTIAL_JOB_SITE);
		brain.forget(MemoryModuleType.LOOK_TARGET);
		villager.setVillagerData(villager.getVillagerData().withProfession(professionEntry));
	}

	@Unique
	private static void onZeroExperience(ServerWorld world, VillagerEntity villager) {
		Object[] result = findProfessionBlock(world, villager);

		if (result == null) {
			return;
		}

		BlockPos professionBlockPos = (BlockPos) result[0];
		Block professionBlock = (Block) result[1];
		RegistryEntry<VillagerProfession> currentProfession = villager.getVillagerData().profession();
		RegistryEntry<VillagerProfession> requiredProfession = Registries.VILLAGER_PROFESSION.getOrThrow(getProfessionByBlock(professionBlock));

		if (currentProfession.value() != requiredProfession.value()) {
			tryClaimProfession(world, villager, requiredProfession, professionBlockPos);
		}
	}

	@Unique
	private static void onNonZeroExperience(ServerWorld world, VillagerEntity villager) {
		Brain<VillagerEntity> brain = villager.getBrain();
		if (brain.getOptionalMemory(MemoryModuleType.JOB_SITE).isEmpty()) {
			Object[] result = findProfessionBlock(world, villager);
			if (result != null) {
				BlockPos professionBlockPos = (BlockPos) result[0];
				Block professionBlock = (Block) result[1];
				RegistryEntry<VillagerProfession> currentProfession = villager.getVillagerData().profession();
				RegistryEntry<VillagerProfession> requiredProfession = Registries.VILLAGER_PROFESSION.getOrThrow(getProfessionByBlock(professionBlock));
				if (currentProfession.value() == requiredProfession.value()) {
					tryReClaimProfession(world, villager, currentProfession, professionBlockPos);
				}
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		VillagerEntity villager = (VillagerEntity) (Object) this;
		if (!(villager.getEntityWorld() instanceof ServerWorld world) || world.getTime() % 10 != 0) {
			return;
		}

		if (getVillagerPostEntity(villager) != null) {
			if (villager.getExperience() > 0) {
				onNonZeroExperience(world, villager);
				return;
			}

			onZeroExperience(world, villager);
		}
	}
}
