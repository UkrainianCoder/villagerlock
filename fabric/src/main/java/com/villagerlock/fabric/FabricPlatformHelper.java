package com.villagerlock.fabric;

import com.villagerlock.VillagerLock;
import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import com.villagerlock.platform.PlatformHelper;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricPlatformHelper implements PlatformHelper {
	@Override
	public <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
		Identifier id = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, name);
		ResourceKey<Block> key = ResourceKey.create(BuiltInRegistries.BLOCK.key(), id);
		T block = factory.apply(properties.setId(key));
		Registry.register(BuiltInRegistries.BLOCK, id, block);
		VillagerLock.LOGGER.info("Registered block with ID: {}", id);
		return () -> block;
	}

	@Override
	public <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> blockSupplier, ResourceKey<CreativeModeTab> tab) {
		Identifier id = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, name);
		ResourceKey<Item> itemKey = ResourceKey.create(BuiltInRegistries.ITEM.key(), id);
		Item.Properties itemSettings = new Item.Properties().setId(itemKey);
		Item item = new BlockItem(blockSupplier.get(), itemSettings);
		Registry.register(BuiltInRegistries.ITEM, id, item);
		CreativeModeTabEvents.modifyOutputEvent(tab).register(itemGroup -> itemGroup.accept(item));
		VillagerLock.LOGGER.info("Registered item with ID: {}", id);
		return () -> item;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Supplier<BlockEntityType<VillagerPostBlockEntity>> registerVillagerPostEntity(String name, BlockEntityType.BlockEntitySupplier<VillagerPostBlockEntity> factory, Supplier<? extends Block>... validBlocks) {
		Identifier id = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, name);
		Block[] blocks = Arrays.stream(validBlocks).map(Supplier::get).toArray(Block[]::new);
		BlockEntityType<VillagerPostBlockEntity> blockEntityType = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				id,
				FabricBlockEntityTypeBuilder.create(factory::create, blocks).build()
		);
		return () -> blockEntityType;
	}
}
