package com.villagerlock.blocks.helpers;

import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class VillagerPostBlockHelper {
	public static VillagerPostBlockEntity getPostEntity(LivingEntity villager) {
		Level world = villager.level();
		BlockPos pos = villager.blockPosition();
		return getPostEntity(world, pos, villager.getUUID());
	}

	public static VillagerPostBlockEntity getPostEntity(Level world, BlockPos pos, UUID uuid) {
		BlockPos[] adjacentPositions = new BlockPos[]{
				pos, pos.below()
		};

		for (BlockPos adjacent : adjacentPositions) {
			BlockEntity blockEntity = world.getBlockEntity(adjacent);
			if (blockEntity instanceof VillagerPostBlockEntity post && post.isOccupied() && uuid.equals(post.getEntityUuid())) {
				return post;
			}
		}

		return null;
	}

	public static boolean isEntityOnPost(Entity entity) {
		return entity.entityTags().contains("locked_on_post");
	}
}
