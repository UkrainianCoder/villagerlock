package com.villagerlock;

import com.villagerlock.blocks.VillagerPostBlock;
import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

import static com.villagerlock.VillagerLock.LOGGER;

public class ModBlocks {
	public static final Identifier VILLAGER_POST_ENTITY_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_entity");
	public static final Identifier VILLAGER_POST_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost");
	public static final Block VILLAGER_POST = register(VILLAGER_POST_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_ITEM = register(VILLAGER_POST_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST, new Item.Properties());

	public static final Identifier VILLAGER_POST_ACACIA_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_acacia");
	public static final Block VILLAGER_POST_ACACIA = register(VILLAGER_POST_ACACIA_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_ACACIA_ITEM = register(VILLAGER_POST_ACACIA_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_ACACIA, new Item.Properties());

	public static final Identifier VILLAGER_POST_BIRCH_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_birch");
	public static final Block VILLAGER_POST_BIRCH = register(VILLAGER_POST_BIRCH_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_BIRCH_ITEM = register(VILLAGER_POST_BIRCH_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_BIRCH, new Item.Properties());

	public static final Identifier VILLAGER_POST_CHERRY_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_cherry");
	public static final Block VILLAGER_POST_CHERRY = register(VILLAGER_POST_CHERRY_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_CHERRY_ITEM = register(VILLAGER_POST_CHERRY_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_CHERRY, new Item.Properties());

	public static final Identifier VILLAGER_POST_CRIMSON_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_crimson");
	public static final Block VILLAGER_POST_CRIMSON = register(VILLAGER_POST_CRIMSON_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_CRIMSON_ITEM = register(VILLAGER_POST_CRIMSON_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_CRIMSON, new Item.Properties());

	public static final Identifier VILLAGER_POST_DARK_OAK_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_dark_oak");
	public static final Block VILLAGER_POST_DARK_OAK = register(VILLAGER_POST_DARK_OAK_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_DARK_OAK_ITEM = register(VILLAGER_POST_DARK_OAK_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_DARK_OAK, new Item.Properties());

	public static final Identifier VILLAGER_POST_JUNGLE_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_jungle");
	public static final Block VILLAGER_POST_JUNGLE = register(VILLAGER_POST_JUNGLE_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_JUNGLE_ITEM = register(VILLAGER_POST_JUNGLE_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_JUNGLE, new Item.Properties());

	public static final Identifier VILLAGER_POST_MANGROVE_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_mangrove");
	public static final Block VILLAGER_POST_MANGROVE = register(VILLAGER_POST_MANGROVE_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_MANGROVE_ITEM = register(VILLAGER_POST_MANGROVE_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_MANGROVE, new Item.Properties());

	public static final Identifier VILLAGER_POST_PALE_OAK_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_pale_oak");
	public static final Block VILLAGER_POST_PALE_OAK = register(VILLAGER_POST_PALE_OAK_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_PALE_OAK_ITEM = register(VILLAGER_POST_PALE_OAK_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_PALE_OAK, new Item.Properties());

	public static final Identifier VILLAGER_POST_SPRUCE_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_spruce");
	public static final Block VILLAGER_POST_SPRUCE = register(VILLAGER_POST_SPRUCE_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_SPRUCE_ITEM = register(VILLAGER_POST_SPRUCE_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_SPRUCE, new Item.Properties());

	public static final Identifier VILLAGER_POST_WARPED_ID = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, "villagerpost_warped");
	public static final Block VILLAGER_POST_WARPED = register(VILLAGER_POST_WARPED_ID, VillagerPostBlock::new, createVillagerPostSettings());
	public static final Item VILLAGER_POST_WARPED_ITEM = register(VILLAGER_POST_WARPED_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, VILLAGER_POST_WARPED, new Item.Properties());

	private static BlockBehaviour.Properties createVillagerPostSettings() {
		return BlockBehaviour.Properties.of()
				.mapColor(MapColor.WOOD)
				.instrument(NoteBlockInstrument.BASS)
				.strength(2.0F, 3.0F)
				.sound(SoundType.WOOD)
				.ignitedByLava();
	}

	private static <T extends Block> T register(Identifier id, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties blockSettings) {
		ResourceKey<Block> key = ResourceKey.create(BuiltInRegistries.BLOCK.key(), id);
		T block = blockFactory.apply(blockSettings.setId(key));
		Registry.register(BuiltInRegistries.BLOCK, id, block);
		LOGGER.info("Registered block with ID: {}", id);
		return block;
	}

	@SuppressWarnings("SameParameterValue")
	private static Item register(Identifier id, ResourceKey<CreativeModeTab> group, Block block, Item.Properties itemSettings) {
		ResourceKey<Item> itemKey = ResourceKey.create(BuiltInRegistries.ITEM.key(), id);
		Item item = new BlockItem(block, itemSettings.setId(itemKey));
		Registry.register(BuiltInRegistries.ITEM, id, item);
		CreativeModeTabEvents.modifyOutputEvent(group).register(itemGroup -> itemGroup.accept(item));

		LOGGER.info("Registered item with ID: {}", id);
		return item;
	}

	@SuppressWarnings("EmptyMethod")
	public static void initialize() {
	}

	public static final BlockEntityType<VillagerPostBlockEntity> VILLAGER_POST_ENTITY = Registry.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE,
			VILLAGER_POST_ENTITY_ID,
			FabricBlockEntityTypeBuilder.create(
					VillagerPostBlockEntity::new,
					VILLAGER_POST,
					VILLAGER_POST_ACACIA,
					VILLAGER_POST_BIRCH,
					VILLAGER_POST_CHERRY,
					VILLAGER_POST_CRIMSON,
					VILLAGER_POST_DARK_OAK,
					VILLAGER_POST_JUNGLE,
					VILLAGER_POST_MANGROVE,
					VILLAGER_POST_PALE_OAK,
					VILLAGER_POST_SPRUCE,
					VILLAGER_POST_WARPED
			).build()
	);
}
