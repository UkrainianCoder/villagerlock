package com.villagerlock;

import com.villagerlock.blocks.VillagerPostBlock;
import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import com.villagerlock.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class ModBlocks {
	public static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("functional_blocks"));

	public static final Supplier<Block> VILLAGER_POST = Services.PLATFORM.registerBlock(
			"villagerpost", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost", VILLAGER_POST, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_ACACIA = Services.PLATFORM.registerBlock(
			"villagerpost_acacia", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_ACACIA_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_acacia", VILLAGER_POST_ACACIA, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_BIRCH = Services.PLATFORM.registerBlock(
			"villagerpost_birch", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_BIRCH_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_birch", VILLAGER_POST_BIRCH, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_CHERRY = Services.PLATFORM.registerBlock(
			"villagerpost_cherry", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_CHERRY_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_cherry", VILLAGER_POST_CHERRY, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_CRIMSON = Services.PLATFORM.registerBlock(
			"villagerpost_crimson", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_CRIMSON_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_crimson", VILLAGER_POST_CRIMSON, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_DARK_OAK = Services.PLATFORM.registerBlock(
			"villagerpost_dark_oak", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_DARK_OAK_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_dark_oak", VILLAGER_POST_DARK_OAK, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_JUNGLE = Services.PLATFORM.registerBlock(
			"villagerpost_jungle", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_JUNGLE_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_jungle", VILLAGER_POST_JUNGLE, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_MANGROVE = Services.PLATFORM.registerBlock(
			"villagerpost_mangrove", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_MANGROVE_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_mangrove", VILLAGER_POST_MANGROVE, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_PALE_OAK = Services.PLATFORM.registerBlock(
			"villagerpost_pale_oak", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_PALE_OAK_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_pale_oak", VILLAGER_POST_PALE_OAK, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_SPRUCE = Services.PLATFORM.registerBlock(
			"villagerpost_spruce", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_SPRUCE_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_spruce", VILLAGER_POST_SPRUCE, FUNCTIONAL_BLOCKS);

	public static final Supplier<Block> VILLAGER_POST_WARPED = Services.PLATFORM.registerBlock(
			"villagerpost_warped", VillagerPostBlock::new, createVillagerPostSettings());
	public static final Supplier<Item> VILLAGER_POST_WARPED_ITEM = Services.PLATFORM.registerBlockItem(
			"villagerpost_warped", VILLAGER_POST_WARPED, FUNCTIONAL_BLOCKS);

	@SuppressWarnings("unchecked")
	public static final Supplier<BlockEntityType<VillagerPostBlockEntity>> VILLAGER_POST_ENTITY = Services.PLATFORM.registerVillagerPostEntity(
			"villagerpost_entity", VillagerPostBlockEntity::new, VILLAGER_POST, VILLAGER_POST_ACACIA,
			VILLAGER_POST_BIRCH, VILLAGER_POST_CHERRY, VILLAGER_POST_CRIMSON, VILLAGER_POST_DARK_OAK,
			VILLAGER_POST_JUNGLE, VILLAGER_POST_MANGROVE, VILLAGER_POST_PALE_OAK, VILLAGER_POST_SPRUCE,
			VILLAGER_POST_WARPED
	);

	private static BlockBehaviour.Properties createVillagerPostSettings() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(
				2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava();
	}

	@SuppressWarnings("EmptyMethod")
	public static void initialize() {
	}
}
