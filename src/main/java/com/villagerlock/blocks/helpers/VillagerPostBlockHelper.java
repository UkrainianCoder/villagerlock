package com.villagerlock.blocks.helpers;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class VillagerPostBlockHelper {
	public static VillagerPostBlockEntity getVillagerPostEntity(ZombieVillagerEntity zombieVilliager) {
		World world = zombieVilliager.getEntityWorld();
		BlockPos pos = zombieVilliager.getBlockPos();
		return getVillagerPostEntity(world, pos, zombieVilliager.getUuid());
	}

	public static VillagerPostBlockEntity getVillagerPostEntity(VillagerEntity villager) {
		World world = villager.getEntityWorld();
		BlockPos pos = villager.getBlockPos();
		return getVillagerPostEntity(world, pos, villager.getUuid());
	}

	public static VillagerPostBlockEntity getVillagerPostEntity(World world, BlockPos pos, UUID uuid) {
		BlockPos[] adjacentPositions = new BlockPos[]{
				pos,
				pos.down()
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
