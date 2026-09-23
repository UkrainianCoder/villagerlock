package com.villagerlock.platform;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

public interface PlatformHelper {
	<T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties);

	<T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> blockSupplier, ResourceKey<CreativeModeTab> tab);

	@SuppressWarnings("unchecked")
	<T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityFactory<T> factory, Supplier<? extends Block>... validBlocks);
}
