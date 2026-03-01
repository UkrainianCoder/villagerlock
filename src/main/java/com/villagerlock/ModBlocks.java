package com.villagerlock;

import com.villagerlock.blocks.VillagerPostBlock;
import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

import static com.villagerlock.VillagerLock.LOGGER;

public class ModBlocks {
	public static final Identifier VILLAGER_POST_ID = Identifier.of(VillagerLock.MOD_ID, "villagerpost");
	public static final Block VILLAGER_POST = register(
			VILLAGER_POST_ID,
			VillagerPostBlock::new,
			AbstractBlock.Settings.create()
					.mapColor(MapColor.OAK_TAN)
					.instrument(NoteBlockInstrument.BASS)
					.strength(2.0F, 3.0F)
					.sounds(BlockSoundGroup.WOOD)
					.burnable()
	);

	public static final Item VILLAGER_POST_ITEM = register(
			VILLAGER_POST_ID,
			VILLAGER_POST,
			new Item.Settings()
	);

	private static <T extends Block> T register(Identifier id, Function<AbstractBlock.Settings, T> blockFactory, AbstractBlock.Settings blockSettings) {
		RegistryKey<Block> key = RegistryKey.of(Registries.BLOCK.getKey(), id);
		T block = blockFactory.apply(blockSettings.registryKey(key));
		Registry.register(Registries.BLOCK, id, block);
		LOGGER.info("Registered block with ID: {}", id);
		return block;
	}

	private static <T extends Block> Item register(Identifier id, Block block, Item.Settings itemSettings) {
		RegistryKey<Item> itemKey = RegistryKey.of(Registries.ITEM.getKey(), id);
		Item item = new BlockItem(block, itemSettings.registryKey(itemKey));
		Registry.register(Registries.ITEM, id, item);
		LOGGER.info("Registered item with ID: {}", id);
		return item;
	}	public static final BlockEntityType<VillagerPostBlockEntity> VILLAGER_POST_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			VILLAGER_POST_ID,
			FabricBlockEntityTypeBuilder.create(VillagerPostBlockEntity::new, VILLAGER_POST).build()
	);

	public static void initialize() {
	}




}
