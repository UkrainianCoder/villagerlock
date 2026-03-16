package com.villagerlock.blocks.helpers;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class VillagerPostBlockHelper {
	public static VillagerPostBlockEntity getVillagerPostEntity(ZombieVillager zombieVilliager) {
		Level world = zombieVilliager.level();
		BlockPos pos = zombieVilliager.blockPosition();
		return getVillagerPostEntity(world, pos, zombieVilliager.getUUID());
	}

	public static VillagerPostBlockEntity getVillagerPostEntity(Villager villager) {
		Level world = villager.level();
		BlockPos pos = villager.blockPosition();
		return getVillagerPostEntity(world, pos, villager.getUUID());
	}

	public static VillagerPostBlockEntity getVillagerPostEntity(Level world, BlockPos pos, UUID uuid) {
		BlockPos[] adjacentPositions = new BlockPos[]{
				pos,
				pos.below()
		};

		for (BlockPos adjacent : adjacentPositions) {
			BlockEntity blockEntity = world.getBlockEntity(adjacent);
			if (blockEntity instanceof VillagerPostBlockEntity post && post.isOccupied() && uuid.equals(post.getEntityUuid())) {
				return post;
			}
		}

		return null;
	}
}
