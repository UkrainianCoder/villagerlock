package com.villagerlock;

import com.villagerlock.blocks.VillagerPostBlock;
import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

import static com.villagerlock.VillagerLock.LOGGER;

public class ModBlocks {
	public static final Identifier VILLAGER_POST_ENTITY_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_entity");
	public static final Identifier VILLAGER_POST_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost");
	public static final Block VILLAGER_POST = register(VILLAGER_POST_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_ITEM = register(VILLAGER_POST_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST, new Item.Settings());

	public static final Identifier VILLAGER_POST_ACACIA_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_acacia");
	public static final Block VILLAGER_POST_ACACIA = register(VILLAGER_POST_ACACIA_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_ACACIA_ITEM = register(VILLAGER_POST_ACACIA_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_ACACIA, new Item.Settings());

	public static final Identifier VILLAGER_POST_BIRCH_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_birch");
	public static final Block VILLAGER_POST_BIRCH = register(VILLAGER_POST_BIRCH_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_BIRCH_ITEM = register(VILLAGER_POST_BIRCH_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_BIRCH, new Item.Settings());

	public static final Identifier VILLAGER_POST_CHERRY_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_cherry");
	public static final Block VILLAGER_POST_CHERRY = register(VILLAGER_POST_CHERRY_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_CHERRY_ITEM = register(VILLAGER_POST_CHERRY_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_CHERRY, new Item.Settings());

	public static final Identifier VILLAGER_POST_CRIMSON_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_crimson");
	public static final Block VILLAGER_POST_CRIMSON = register(VILLAGER_POST_CRIMSON_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_CRIMSON_ITEM = register(VILLAGER_POST_CRIMSON_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_CRIMSON, new Item.Settings());

	public static final Identifier VILLAGER_POST_DARK_OAK_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_dark_oak");
	public static final Block VILLAGER_POST_DARK_OAK = register(VILLAGER_POST_DARK_OAK_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_DARK_OAK_ITEM = register(VILLAGER_POST_DARK_OAK_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_DARK_OAK, new Item.Settings());

	public static final Identifier VILLAGER_POST_JUNGLE_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_jungle");
	public static final Block VILLAGER_POST_JUNGLE = register(VILLAGER_POST_JUNGLE_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_JUNGLE_ITEM = register(VILLAGER_POST_JUNGLE_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_JUNGLE, new Item.Settings());

	public static final Identifier VILLAGER_POST_MANGROVE_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_mangrove");
	public static final Block VILLAGER_POST_MANGROVE = register(VILLAGER_POST_MANGROVE_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_MANGROVE_ITEM = register(VILLAGER_POST_MANGROVE_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_MANGROVE, new Item.Settings());

	public static final Identifier VILLAGER_POST_PALE_OAK_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_pale_oak");
	public static final Block VILLAGER_POST_PALE_OAK = register(VILLAGER_POST_PALE_OAK_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_PALE_OAK_ITEM = register(VILLAGER_POST_PALE_OAK_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_PALE_OAK, new Item.Settings());

	public static final Identifier VILLAGER_POST_SPRUCE_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_spruce");
	public static final Block VILLAGER_POST_SPRUCE = register(VILLAGER_POST_SPRUCE_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_SPRUCE_ITEM = register(VILLAGER_POST_SPRUCE_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_SPRUCE, new Item.Settings());

	public static final Identifier VILLAGER_POST_WARPED_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost_warped");
	public static final Block VILLAGER_POST_WARPED = register(VILLAGER_POST_WARPED_ID, VillagerPostBlock::new, createVilliagerPostSettings());
	public static final Item VILLAGER_POST_WARPED_ITEM = register(VILLAGER_POST_WARPED_ID, ItemGroups.FUNCTIONAL, VILLAGER_POST_WARPED, new Item.Settings());

	private static AbstractBlock.Settings createVilliagerPostSettings() {
		return AbstractBlock.Settings.create()
				.mapColor(MapColor.OAK_TAN)
				.instrument(NoteBlockInstrument.BASS)
				.strength(2.0F, 3.0F)
				.sounds(BlockSoundGroup.WOOD)
				.burnable();
	}

	private static <T extends Block> T register(Identifier id, Function<AbstractBlock.Settings, T> blockFactory, AbstractBlock.Settings blockSettings) {
		RegistryKey<Block> key = RegistryKey.of(Registries.BLOCK.getKey(), id);
		T block = blockFactory.apply(blockSettings.registryKey(key));
		Registry.register(Registries.BLOCK, id, block);
		LOGGER.info("Registered block with ID: {}", id);
		return block;
	}

	private static <T extends Block> Item register(Identifier id, RegistryKey<ItemGroup> group, Block block, Item.Settings itemSettings) {
		RegistryKey<Item> itemKey = RegistryKey.of(Registries.ITEM.getKey(), id);
		Item item = new BlockItem(block, itemSettings.registryKey(itemKey));
		Registry.register(Registries.ITEM, id, item);
		ItemGroupEvents.modifyEntriesEvent(group).register((itemGroup) -> {
			itemGroup.add(item);
		});

		LOGGER.info("Registered item with ID: {}", id);
		return item;
	}

	public static void initialize() {
	}

	public static final BlockEntityType<VillagerPostBlockEntity> VILLAGER_POST_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
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
