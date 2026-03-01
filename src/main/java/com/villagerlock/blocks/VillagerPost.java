package com.villagerlock.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class VillagerPost extends Block {
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
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected VoxelShape getInsideCollisionShape(BlockState state, BlockView world, BlockPos pos, Entity entity) {
		return SHAPE;
	}
}
