package com.villagerlock.blocks;

import com.mojang.serialization.MapCodec;
import com.villagerlock.ModBlocks;
import com.villagerlock.blocks.entities.VillagerPostEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;
import org.jspecify.annotations.Nullable;

public class VillagerPost extends BlockWithEntity implements BlockEntityProvider {
	private static final VoxelShape SHAPE;

	static {
		VoxelShape part1 = Block.createCuboidShape(12, 0, 0, 16, 3, 4);
		VoxelShape part2 = Block.createCuboidShape(12, 0, 12, 16, 3, 16);
		VoxelShape part3 = Block.createCuboidShape(0, 0, 0, 4, 3, 4);
		VoxelShape part4 = Block.createCuboidShape(0, 0, 12, 4, 3, 16);
		VoxelShape part5 = Block.createCuboidShape(13, 0, 4, 16, 2, 12);
		VoxelShape part6 = Block.createCuboidShape(4, 0, 13, 12, 2, 16);
		VoxelShape part7 = Block.createCuboidShape(0, 0, 4, 3, 2, 12);
		VoxelShape part8 = Block.createCuboidShape(4, 0, 0, 12, 2, 3);
		VoxelShape part9 = Block.createCuboidShape(3, 0, 3, 13, 1, 13);
		SHAPE = VoxelShapes.union(part1, part2, part3, part4, part5, part6, part7, part8, part9);
	}

	public VillagerPost(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new VillagerPostEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient() && type == ModBlocks.VILLAGER_POST_ENTITY) {
			return (w, pos, s, be) -> {
				if (be instanceof VillagerPostEntity post) {
					VillagerPostEntity.onTick(w, pos, s, post);
				}
			};
		}

		return super.getTicker(world, state, type);
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof VillagerPostEntity postBlockEntity) {
			postBlockEntity.unseat(world);
		}

		return super.onBreak(world, pos, state, player);
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bool) {
		if (world.isClient()) {
			return;
		}

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof VillagerPostEntity postBlockEntity)) {
			return;
		}

		if (!postBlockEntity.isOccupied() && entity.getVehicle() == null && entity instanceof VillagerEntity) {
			postBlockEntity.seat(world, entity);
		}
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return null;
	}

	@Override
	public @Nullable <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
		return super.getGameEventListener(world, blockEntity);
	}

	@Override
	public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
		return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
	}

	@Override
	public boolean isEnabled(FeatureSet enabledFeatures) {
		return super.isEnabled(enabledFeatures);
	}
}
