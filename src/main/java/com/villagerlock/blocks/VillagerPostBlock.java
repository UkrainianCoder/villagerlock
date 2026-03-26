package com.villagerlock.blocks;

import com.mojang.serialization.MapCodec;
import com.villagerlock.ModBlocks;
import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class VillagerPostBlock extends BaseEntityBlock implements EntityBlock, SimpleWaterloggedBlock {
	private static final VoxelShape SHAPE;

	static {
		VoxelShape part1 = Block.box(12, 0, 0, 16, 3, 4);
		VoxelShape part2 = Block.box(12, 0, 12, 16, 3, 16);
		VoxelShape part3 = Block.box(0, 0, 0, 4, 3, 4);
		VoxelShape part4 = Block.box(0, 0, 12, 4, 3, 16);
		VoxelShape part5 = Block.box(13, 0, 4, 16, 2, 12);
		VoxelShape part6 = Block.box(4, 0, 13, 12, 2, 16);
		VoxelShape part7 = Block.box(0, 0, 4, 3, 2, 12);
		VoxelShape part8 = Block.box(4, 0, 0, 12, 2, 3);
		VoxelShape part9 = Block.box(3, 0, 3, 13, 1, 13);
		SHAPE = Shapes.or(part1, part2, part3, part4, part5, part6, part7, part8, part9);
	}

	public VillagerPostBlock(BlockBehaviour.Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(BlockStateProperties.WATERLOGGED, false)
				.setValue(BlockStateProperties.FACING, Direction.NORTH)
				.setValue(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED);
		builder.add(BlockStateProperties.FACING);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter world, @NonNull BlockPos pos, @NonNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
		return new VillagerPostBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
		if (!world.isClientSide() && type == ModBlocks.VILLAGER_POST_ENTITY) {
			return (tickerWorld, tickerPos, _, customEntity) -> {
				if (customEntity instanceof VillagerPostBlockEntity blockEntity) {
					if (blockEntity.isOccupied()) {
						Entity rider = tickerWorld.getEntity(blockEntity.getEntityUuid());
						if (rider == null || rider.distanceToSqr(tickerPos.getX() + 0.5, tickerPos.getY(), tickerPos.getZ() + 0.5) > 2.0D) {
							blockEntity.unseat(tickerWorld, false);
						}
					}
				}
			};
		}

		return super.getTicker(world, state, type);
	}

	@Override
	public @NonNull BlockState playerWillDestroy(Level world, @NonNull BlockPos pos, @NonNull BlockState state, @NonNull Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof VillagerPostBlockEntity postBlockEntity) {
			postBlockEntity.unseat(world, true);
		}

		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	protected void entityInside(@NonNull BlockState state, Level world, @NonNull BlockPos pos, @NonNull Entity entity, @NonNull InsideBlockEffectApplier handler, boolean bool) {
		if (world.isClientSide()) {
			return;
		}

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof VillagerPostBlockEntity postBlockEntity)) {
			return;
		}

		if (!postBlockEntity.isOccupied() && !VillagerPostBlockEntity.isEntityOnPost(entity) && entity.getVehicle() == null && entity instanceof LivingEntity livingEntity && !livingEntity.isSleeping() && (entity instanceof Villager || entity instanceof ZombieVillager)) {
			postBlockEntity.seat(world, entity);
		}
	}

	@Override
	public void neighborChanged(@NonNull BlockState state, Level world, @NonNull BlockPos pos, @NonNull Block sourceBlock, Orientation wireOrientation, boolean notify) {
		if (!world.isClientSide()) {
			boolean hasSignal = world.hasNeighborSignal(pos);
			if (hasSignal && world.getBlockEntity(pos) instanceof VillagerPostBlockEntity post && post.isOccupied()) {
				post.unseat(world, true);
			}

			if (hasSignal != state.getValue(BlockStateProperties.POWERED)) {
				world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, hasSignal), 3);
			}
		}
	}

	@Override
	public @NonNull FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public @NonNull BlockState updateShape(BlockState state, @NonNull LevelReader world, @NonNull ScheduledTickAccess tickView, @NonNull BlockPos pos, @NonNull Direction direction, @NonNull BlockPos neighborPos, @NonNull BlockState neighborState, @NonNull RandomSource random) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}

		return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState()
				.setValue(BlockStateProperties.FACING, ctx.getHorizontalDirection().getOpposite())
				.setValue(BlockStateProperties.POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
	}

	@Override
	protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
		return MapCodec.unit(this);
	}

	@Override
	public @Nullable <T extends BlockEntity> GameEventListener getListener(@NonNull ServerLevel world, T blockEntity) {
		return super.getListener(world, blockEntity);
	}

	@Override
	public boolean isEnabled(@NonNull FeatureFlagSet enabledFeatures) {
		return super.isEnabled(enabledFeatures);
	}
}
