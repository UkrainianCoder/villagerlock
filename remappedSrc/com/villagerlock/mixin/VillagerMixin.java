package com.villagerlock.mixin;

import com.villagerlock.blocks.helpers.VillagerPostBlockHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

@Mixin(Villager.class)
public class VillagerMixin {
	@Unique
	private static final Map<Block, ResourceKey<VillagerProfession>> BLOCK_PROFESSION_MAP = Map.ofEntries(
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
	private static ResourceKey<VillagerProfession> getProfessionByBlock(Block block) {
		if (block == null) {
			return VillagerProfession.NONE;
		}

		return BLOCK_PROFESSION_MAP.getOrDefault(block, VillagerProfession.NONE);
	}

	@Unique
	private static Object[] findProfessionBlock(ServerLevel world, Villager villager) {
		BlockPos pos = villager.blockPosition();
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
	private static void tryClaimProfession(ServerLevel world, Villager villager, Holder<VillagerProfession> profession, BlockPos professionPos) {
		Brain<Villager> brain = villager.getBrain();
		brain.eraseMemory(MemoryModuleType.JOB_SITE);
		brain.eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);

		GlobalPos globalPos = GlobalPos.of(world.dimension(), professionPos);
		world.registryAccess().lookupOrThrow(Registries.POINT_OF_INTEREST_TYPE)
				.listElements()
				.findFirst()
				.ifPresent(poiType -> {
					PoiManager poiStorage = world.getPoiManager();
					if (poiStorage.getType(professionPos).isEmpty()) {
						poiStorage.add(professionPos, poiType);
					}

					brain.setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, globalPos);
					brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(professionPos));
				});
	}

	@Unique
	private static void tryReClaimProfession(ServerLevel world, Villager villager, Holder<VillagerProfession> profession, BlockPos professionPos) {
		Brain<Villager> brain = villager.getBrain();
		GlobalPos globalPos = GlobalPos.of(world.dimension(), professionPos);
		brain.setMemory(MemoryModuleType.JOB_SITE, globalPos);
	}

	@Unique
	private static void tryRemoveProfession(ServerLevel world, Villager villager) {
		Holder<VillagerProfession> professionEntry = BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(VillagerProfession.NONE);
		Brain<Villager> brain = villager.getBrain();
		brain.eraseMemory(MemoryModuleType.JOB_SITE);
		brain.eraseMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
		brain.eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
		brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
		villager.setVillagerData(villager.getVillagerData().withProfession(professionEntry));
	}

	@Unique
	private static void onZeroExperience(ServerLevel world, Villager villager) {
		Object[] result = findProfessionBlock(world, villager);

		if (result == null) {
			return;
		}

		BlockPos professionBlockPos = (BlockPos) result[0];
		Block professionBlock = (Block) result[1];
		Holder<VillagerProfession> currentProfession = villager.getVillagerData().profession();
		Holder<VillagerProfession> requiredProfession = BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(getProfessionByBlock(professionBlock));

		if (currentProfession.value() != requiredProfession.value()) {
			tryClaimProfession(world, villager, requiredProfession, professionBlockPos);
		}
	}

	@Unique
	private static void onNonZeroExperience(ServerLevel world, Villager villager) {
		Brain<Villager> brain = villager.getBrain();
		if (brain.getMemoryInternal(MemoryModuleType.JOB_SITE).isEmpty()) {
			Object[] result = findProfessionBlock(world, villager);
			if (result != null) {
				BlockPos professionBlockPos = (BlockPos) result[0];
				Block professionBlock = (Block) result[1];
				Holder<VillagerProfession> currentProfession = villager.getVillagerData().profession();
				Holder<VillagerProfession> requiredProfession = BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(getProfessionByBlock(professionBlock));
				if (currentProfession.value() == requiredProfession.value()) {
					tryReClaimProfession(world, villager, currentProfession, professionBlockPos);
				}
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		Villager villager = (Villager) (Object) this;
		if (!(villager.level() instanceof ServerLevel world) || world.getGameTime() % 10 != 0) {
			return;
		}

		if (VillagerPostBlockHelper.getVillagerPostEntity(villager) != null) {
			if (villager.getVillagerXp() > 0) {
				onNonZeroExperience(world, villager);
				return;
			}

			onZeroExperience(world, villager);
		}
	}
}
