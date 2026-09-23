package com.villagerlock;

import com.villagerlock.blocks.VillagerPostBlock;
import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import com.villagerlock.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
	private static final ResourceKey<CreativeModeTab> CREATIVE_MODE_TAB_RESOURCE_KEY = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB,
			Identifier.withDefaultNamespace("functional_blocks")
	);

	private static final List<Supplier<Block>> VILLAGER_POSTS = new ArrayList<>();
	public static final Supplier<Block> VILLAGER_POST = register("villagerpost");
	public static final Supplier<Block> VILLAGER_POST_ACACIA = register("villagerpost_acacia");
	public static final Supplier<Block> VILLAGER_POST_BIRCH = register("villagerpost_birch");
	public static final Supplier<Block> VILLAGER_POST_CHERRY = register("villagerpost_cherry");
	public static final Supplier<Block> VILLAGER_POST_CRIMSON = register("villagerpost_crimson");
	public static final Supplier<Block> VILLAGER_POST_DARK_OAK = register("villagerpost_dark_oak");
	public static final Supplier<Block> VILLAGER_POST_JUNGLE = register("villagerpost_jungle");
	public static final Supplier<Block> VILLAGER_POST_MANGROVE = register("villagerpost_mangrove");
	public static final Supplier<Block> VILLAGER_POST_PALE_OAK = register("villagerpost_pale_oak");
	public static final Supplier<Block> VILLAGER_POST_SPRUCE = register("villagerpost_spruce");
	public static final Supplier<Block> VILLAGER_POST_WARPED = register("villagerpost_warped");
	public static final Supplier<Block> VILLAGER_POST_POPLAR = VillagerLock.IS_MC_263
			? register("villagerpost_poplar")
			: null;

	@SuppressWarnings("unchecked")
	public static final Supplier<BlockEntityType<VillagerPostBlockEntity>> VILLAGER_POST_ENTITY = Services.PLATFORM.registerBlockEntity("villagerpost_entity",
			VillagerPostBlockEntity::new,
			VILLAGER_POSTS.toArray(Supplier[]::new)
	);

	private static Supplier<Block> register(String name) {
		Supplier<Block> block = Services.PLATFORM.registerBlock(
				name,
				VillagerPostBlock::new,
				createVillagerPostSettings()
		);

		Services.PLATFORM.registerBlockItem(name, block, CREATIVE_MODE_TAB_RESOURCE_KEY);
		VILLAGER_POSTS.add(block);
		return block;
	}

	private static BlockBehaviour.Properties createVillagerPostSettings() {
		return BlockBehaviour.Properties.of()
										.mapColor(MapColor.WOOD)
										.instrument(NoteBlockInstrument.BASS)
										.strength(2.0F, 3.0F)
										.sound(SoundType.WOOD)
										.ignitedByLava();
	}

	public static void initialize() {
		VillagerLock.LOGGER.info("Registered {} blocks", VILLAGER_POSTS.size());
	}
}
