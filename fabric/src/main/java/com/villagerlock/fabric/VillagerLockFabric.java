package com.villagerlock.fabric;

import com.villagerlock.VillagerLock;
import net.fabricmc.api.ModInitializer;

public class VillagerLockFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		VillagerLock.init();
	}
}
