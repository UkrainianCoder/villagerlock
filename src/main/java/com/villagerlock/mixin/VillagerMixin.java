package com.villagerlock.mixin;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
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

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		VillagerEntity villager = (VillagerEntity) (Object) this;
		if (!(villager.getEntityWorld() instanceof ServerWorld world) || world.getTime() % 10 != 0) {
			return;
		}

		if (villager.getExperience() > 0) {
			return;
		}

		VillagerPostBlockEntity post = getVillagerPostEntity(villager);
	}
}
