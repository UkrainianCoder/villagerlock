package com.villagerlock.neoforge;

import com.villagerlock.VillagerLock;
import com.villagerlock.blocks.entities.VillagerPostBlockEntity;
import com.villagerlock.platform.PlatformHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NeoForgePlatformHelper implements PlatformHelper {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(VillagerLock.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VillagerLock.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
			Registries.BLOCK_ENTITY_TYPE,
			VillagerLock.MOD_ID
	);

	private static final List<Supplier<? extends Item>> TAB_ITEMS = new ArrayList<>();
	private static ResourceKey<CreativeModeTab> TARGET_TAB;

	public static void init(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		BLOCK_ENTITY_TYPES.register(modEventBus);
		modEventBus.addListener(NeoForgePlatformHelper::onBuildCreativeTabContents);
	}

	private static void onBuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
		if (TARGET_TAB != null && event.getTabKey().equals(TARGET_TAB)) {
			for (Supplier<? extends Item> itemSupplier : TAB_ITEMS) {
				event.accept(itemSupplier.get());
			}
		}
	}

	@Override
	public <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
		Identifier id = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, name);
		ResourceKey<Block> key = ResourceKey.create(BuiltInRegistries.BLOCK.key(), id);
		DeferredBlock<T> block = BLOCKS.register(name, () -> factory.apply(properties.setId(key)));
		return block;
	}

	@Override
	public <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> blockSupplier, ResourceKey<CreativeModeTab> tab) {
		TARGET_TAB = tab;
		Identifier id = Identifier.fromNamespaceAndPath(VillagerLock.MOD_ID, name);
		ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(), id);
		DeferredItem<Item> item = ITEMS.register(
				name,
				() -> new BlockItem(
						blockSupplier.get(),
						new Item.Properties().setId(key)
				)
		);

		TAB_ITEMS.add(item);
		return item;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Supplier<BlockEntityType<VillagerPostBlockEntity>> registerVillagerPostEntity(String name, BlockEntityType.BlockEntitySupplier<VillagerPostBlockEntity> factory, Supplier<? extends Block>... validBlocks) {
		DeferredHolder<BlockEntityType<?>, BlockEntityType<VillagerPostBlockEntity>> holder = BLOCK_ENTITY_TYPES.register(
				name, () -> {
					Set<Block> blockSet = Arrays.stream(validBlocks).map(Supplier::get).collect(Collectors.toSet());
					return new BlockEntityType<>(factory::create, blockSet);
				}
		);

		return holder;
	}
}
